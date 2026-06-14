package com.fifa.simpa.domain.engine

import com.fifa.simpa.domain.model.*
import com.fifa.simpa.domain.modifier.*
import kotlin.math.abs
import kotlin.random.Random

/**
 * Minute-by-Minute Monte Carlo Event Loop
 *
 * Runs a 90-minute computational loop (or 120 for knockout).
 * Each minute, determines possession shifts, tackles, passes,
 * or shot events based on real-time midfield ratings.
 */
class MonteCarloMatchEngine(
    private val poissonEngine: PoissonGoalEngine = PoissonGoalEngine(),
    private val fatigueEngine: FatigueEngine = FatigueEngine(),
    private val momentumEngine: MomentumEngine = MomentumEngine(),
    private val weatherEngine: WeatherModifier = WeatherModifier(),
    private val refereeEngine: RefereeEngine = RefereeEngine()
) {

    data class MatchSimulationResult(
        val match: Match,
        val events: List<SimulatedEvent>,
        val homeXG: Double,
        val awayXG: Double
    )

    /**
     * Run a full match simulation with minute-by-minute Monte Carlo process.
     */
    fun simulateMatch(
        match: Match,
        random: Random = Random
    ): MatchSimulationResult {
        val isKnockout = match.stage.ordinal >= MatchStage.ROUND_OF_16.ordinal
        val totalMinutes = if (isKnockout) 120 else 90

        // Calculate base expected goals for the match
        val baseLambdaHome = poissonEngine.calculateExpectedGoals(
            match.homeTeam, match.awayTeam, homeAdvantage = true
        )
        val baseLambdaAway = poissonEngine.calculateExpectedGoals(
            match.awayTeam, match.homeTeam, homeAdvantage = false
        )

        var homeScore = 0
        var awayScore = 0
        var homeShots = 0
        var awayShots = 0
        var homeShotsOnTarget = 0
        var awayShotsOnTarget = 0
        var homePossession = 50
        var awayPossession = 50

        var mutableHomeXG = 0.0
        var mutableAwayXG = 0.0

        val homeGoals = mutableListOf<GoalEvent>()
        val awayGoals = mutableListOf<GoalEvent>()
        val homeCards = mutableListOf<CardEvent>()
        val awayCards = mutableListOf<CardEvent>()
        val homeInjuries = mutableListOf<InjuryEvent>()
        val awayInjuries = mutableListOf<InjuryEvent>()
        val substitutions = mutableListOf<SubstitutionEvent>()
        val events = mutableListOf<SimulatedEvent>()

        var currentHomeLambda = baseLambdaHome
        var currentAwayLambda = baseLambdaAway
        var homeMorale = 0.5f
        var awayMorale = 0.5f

        // Apply weather modifiers to pacing
        val (_, shotMod, _, _) = weatherEngine.applyWeatherEffects(match.weather)

        // Apply referee strictness
        val refereeCoeff = refereeEngine.getFoulProbabilityMultiplier(match.refereeStrictness)

        events.add(SimulatedEvent(
            minute = 0,
            eventType = EventType.KICK_OFF,
            description = "Match kicked off between ${match.homeTeam.name} and ${match.awayTeam.name}"
        ))

        // Track which team has possession for the loop
        var possessionTeam: String = if (random.nextBoolean()) match.homeTeam.id else match.awayTeam.id

        // Minute-by-minute simulation
        for (minute in 1..totalMinutes) {
            val isHalftime = minute == 45

            // Half time break
            if (isHalftime) {
                events.add(SimulatedEvent(
                    minute = 45,
                    eventType = EventType.HALF_TIME,
                    description = "Half time: ${match.homeTeam.shortName} $homeScore - $awayScore ${match.awayTeam.shortName}",
                    impact = 0.5
                ))
                continue
            }

            // Every 2-4 minutes, attempt a possession shift
            if (random.nextInt(3) == 0) {
                // Midfield battle determines possession swing
                val homeMid = match.homeTeam.stats.midfieldStrength
                val awayMid = match.awayTeam.stats.midfieldStrength
                val midRatio = homeMid / (homeMid + awayMid)

                // Apply fatigue impact on midfield
                val fatigueMidModifier = fatigueEngine.getMidfieldFatigueModifier(minute)

                possessionTeam = if (random.nextDouble() < midRatio * fatigueMidModifier) {
                    match.homeTeam.id
                } else {
                    match.awayTeam.id
                }

                // Update possession percentages
                if (possessionTeam == match.homeTeam.id) {
                    homePossession = (homePossession + (50 - homePossession) * 0.1 + random.nextInt(-3, 3)).toInt().coerceIn(35, 65)
                    awayPossession = 100 - homePossession
                } else {
                    awayPossession = (awayPossession + (50 - awayPossession) * 0.1 + random.nextInt(-3, 3)).toInt().coerceIn(35, 65)
                    homePossession = 100 - awayPossession
                }
            }

            // Fatigue value for this minute
            val fatigue = fatigueEngine.calculateFatigueAtMinute(minute, totalMinutes)

            // Try a tackle event (higher probability when not in possession)
            if (random.nextDouble() < 0.15 * refereeCoeff) {
                val tacklingTeam = if (possessionTeam == match.homeTeam.id) match.awayTeam else match.homeTeam
                val tacklingTeamId = if (possessionTeam == match.homeTeam.id) match.awayTeam.id else match.homeTeam.id
                val tackleSuccess = random.nextDouble() < 0.65 - fatigue * 0.1

                if (tackleSuccess) {
                    possessionTeam = tacklingTeamId
                    events.add(SimulatedEvent(
                        minute = minute,
                        eventType = EventType.TACKLE,
                        description = "${tacklingTeam.name} win the ball back!",
                        teamId = tacklingTeamId,
                        impact = 0.3
                    ))
                }

                // Foul chance
                if (!tackleSuccess && random.nextDouble() < 0.25 * refereeCoeff) {
                    val isYellow = random.nextDouble() < 0.12 * refereeCoeff
                    val isSecondYellow = false

                    if (isYellow) {
                        val card = CardEvent("", "${tacklingTeam.name} player", minute, true, isSecondYellow)
                        if (tacklingTeam.id == match.homeTeam.id) homeCards.add(card)
                        else awayCards.add(card)

                        events.add(SimulatedEvent(
                            minute = minute,
                            eventType = EventType.CARD,
                            description = "Yellow card for ${tacklingTeam.name}",
                            teamId = tacklingTeam.id,
                            impact = 0.4
                        ))
                    }

                    events.add(SimulatedEvent(
                        minute = minute,
                        eventType = EventType.FOUL,
                        description = "Foul committed by ${tacklingTeam.name}",
                        teamId = tacklingTeam.id,
                        impact = 0.1
                    ))
                }
            }

            // Pass completion attempt
            if (random.nextDouble() < 0.35) {
                val passTeam = if (possessionTeam == match.homeTeam.id) match.homeTeam else match.awayTeam
                val passTeamId = passTeam.id

                // Pass completion rate drops with fatigue
                val basePassRate = 0.82
                val passSuccess = random.nextDouble() < basePassRate - fatigue * 0.2 - (if (passTeamId == match.homeTeam.id) 0.0 else 0.0)

                if (!passSuccess) {
                    // Turnover
                    possessionTeam = if (passTeamId == match.homeTeam.id) match.awayTeam.id else match.homeTeam.id

                    events.add(SimulatedEvent(
                        minute = minute,
                        eventType = EventType.PASS,
                        description = "Poor pass by ${passTeam.name}, possession lost",
                        teamId = passTeamId,
                        impact = 0.15
                    ))
                }
            }

            // Shot attempt - probability based on attack strength and possession
            val shotProb = 0.04 * (if (possessionTeam == match.homeTeam.id)
                match.homeTeam.stats.attackStrength / 50.0
            else
                match.awayTeam.stats.attackStrength / 50.0)

            // Apply momentum effect for trailing teams
            val scoreDiff = if (possessionTeam == match.homeTeam.id) {
                homeScore - awayScore
            } else {
                awayScore - homeScore
            }

            val momentumMod = if (scoreDiff < 0) {
                // Trailing team pushes harder
                val aggMod = momentumEngine.calculateAggressionModifier(
                    if (possessionTeam == match.homeTeam.id) homeMorale else awayMorale,
                    abs(scoreDiff)
                )
                aggMod * 1.3
            } else 1.0

            if (random.nextDouble() < shotProb * momentumMod * shotMod) {
                val shootingTeam = if (possessionTeam == match.homeTeam.id) match.homeTeam else match.awayTeam
                val defendingTeam = if (possessionTeam == match.homeTeam.id) match.awayTeam else match.homeTeam

                if (possessionTeam == match.homeTeam.id) homeShots++ else awayShots++

                // Determine shot type and xG
                val shotType = determineShotType(random)
                val distance = when (shotType) {
                    ShotType.TAP_IN -> random.nextDouble() * 3.0
                    ShotType.CLOSE_RANGE -> random.nextDouble() * 8.0 + 3.0
                    ShotType.PENALTY_AREA -> random.nextDouble() * 10.0 + 8.0
                    ShotType.LONG_RANGE -> random.nextDouble() * 10.0 + 18.0
                    ShotType.VERY_LONG_RANGE -> random.nextDouble() * 10.0 + 28.0
                    ShotType.HEADER -> random.nextDouble() * 10.0 + 3.0
                    ShotType.FREE_KICK -> random.nextDouble() * 10.0 + 18.0
                    ShotType.PENALTY -> 12.0
                }

                val angle = random.nextDouble() * 45.0
                val shotXG = poissonEngine.calculateShotXG(shotType, distance, angle, shotType == ShotType.HEADER)

                // Accumulate xG
                if (possessionTeam == match.homeTeam.id) mutableHomeXG += shotXG
                else mutableAwayXG += shotXG

                // Check if shot is on target
                val onTarget = random.nextDouble() < 0.4 + (shootingTeam.stats.attackStrength / 150.0)
                if (onTarget) {
                    if (possessionTeam == match.homeTeam.id) homeShotsOnTarget++ else awayShotsOnTarget++
                }

                // Determine if shot becomes a goal
                val goalThreshold = shotXG * (1.0 + (defendingTeam.stats.defenseStrength / 100.0) * 0.3)
                val goalScored = random.nextDouble() < goalThreshold

                if (goalScored) {
                    val goalEvent = GoalEvent(
                        playerId = "${shootingTeam.id}-P",
                        playerName = "${shootingTeam.name} Player",
                        minute = minute,
                        xG = shotXG
                    )
                    if (possessionTeam == match.homeTeam.id) {
                        homeScore++
                        homeGoals.add(goalEvent)
                        homeMorale = (homeMorale + 0.1f).coerceAtMost(1.0f)
                        awayMorale = (awayMorale - 0.1f).coerceAtLeast(0.0f)
                    } else {
                        awayScore++
                        awayGoals.add(goalEvent)
                        awayMorale = (awayMorale + 0.1f).coerceAtMost(1.0f)
                        homeMorale = (homeMorale - 0.1f).coerceAtLeast(0.0f)
                    }

                    events.add(SimulatedEvent(
                        minute = minute,
                        eventType = EventType.GOAL,
                        description = "GOAL! ${shootingTeam.name} scores! (xG: ${"%.2f".format(shotXG)})",
                        teamId = shootingTeam.id,
                        impact = 1.0
                    ))
                } else if (onTarget) {
                    events.add(SimulatedEvent(
                        minute = minute,
                        eventType = EventType.SAVE,
                        description = "Save by ${defendingTeam.name} goalkeeper! ($shotType, xG: ${"%.2f".format(shotXG)})",
                        teamId = defendingTeam.id,
                        impact = 0.3
                    ))
                } else {
                    events.add(SimulatedEvent(
                        minute = minute,
                        eventType = EventType.SHOT,
                        description = "Shot by ${shootingTeam.name} goes wide. ($shotType, xG: ${"%.2f".format(shotXG)})",
                        teamId = shootingTeam.id,
                        impact = 0.2
                    ))
                }

                // Reset possession after shot
                possessionTeam = if (random.nextBoolean()) match.homeTeam.id else match.awayTeam.id
            }

            // Simulate substitution at 55-80 minutes
            if (minute in 55..80 && random.nextInt(15) == 0) {
                val subTeam = if (random.nextBoolean()) match.homeTeam else match.awayTeam
                substitutions.add(SubstitutionEvent(
                    minute = minute,
                    playerOffId = "${subTeam.id}-OFF",
                    playerOffName = "${subTeam.name} Player",
                    playerOnId = "${subTeam.id}-ON",
                    playerOnName = "${subTeam.name} Sub",
                    teamId = subTeam.id
                ))
                events.add(SimulatedEvent(
                    minute = minute,
                    eventType = EventType.SUBSTITUTION,
                    description = "Substitution for ${subTeam.name}",
                    teamId = subTeam.id,
                    impact = 0.2
                ))
            }

            // Injury chance (rare)
            if (minute in 10..(totalMinutes - 5) && random.nextInt(250) == 0) {
                val injuredTeam = if (random.nextBoolean()) match.homeTeam else match.awayTeam
                val injury = InjuryEvent(
                    playerId = "${injuredTeam.id}-INJ",
                    playerName = "${injuredTeam.name} Player",
                    minute = minute,
                    severity = InjurySeverity.MODERATE
                )
                if (injuredTeam.id == match.homeTeam.id) homeInjuries.add(injury)
                else awayInjuries.add(injury)

                events.add(SimulatedEvent(
                    minute = minute,
                    eventType = EventType.INJURY,
                    description = "Injury for ${injuredTeam.name}! Medical staff on the pitch.",
                    teamId = injuredTeam.id,
                    impact = 0.4
                ))
            }

            // Momentum shifts
            val momentumDelta = momentumEngine.calculateMomentumShift(
                minute, homeScore, awayScore, totalMinutes
            )
            if (momentumDelta != 0.0) {
                if (momentumDelta > 0) {
                    homeMorale = (homeMorale + 0.03f).coerceAtMost(1.0f)
                    currentHomeLambda *= (1.0 + momentumDelta)
                } else {
                    awayMorale = (awayMorale + 0.03f).coerceAtMost(1.0f)
                    currentAwayLambda *= (1.0 + abs(momentumDelta))
                }
            }
        }

        // Final whistle
        events.add(SimulatedEvent(
            minute = totalMinutes,
            eventType = EventType.FULL_TIME,
            description = "Full time! ${match.homeTeam.shortName} $homeScore - $awayScore ${match.awayTeam.shortName}",
            impact = 1.0
        ))

        // Handle penalty shootout for knockout draws
        var homePenaltyScore: Int? = null
        var awayPenaltyScore: Int? = null

        if (isKnockout && homeScore == awayScore) {
            events.add(SimulatedEvent(
                minute = totalMinutes,
                eventType = EventType.PENALTY_SHOOTOUT,
                description = "Penalty shootout!",
                impact = 1.0
            ))

            val (hPen, aPen) = simulatePenaltyShootout(random)
            homePenaltyScore = hPen
            awayPenaltyScore = aPen
        }

        val finalMatch = match.copy(
            status = MatchStatus.FINISHED,
            homeScore = homeScore,
            awayScore = awayScore,
            minute = totalMinutes,
            homeXG = mutableHomeXG,
            awayXG = mutableAwayXG,
            homePossession = homePossession,
            awayPossession = awayPossession,
            homeShots = homeShots,
            awayShots = awayShots,
            homeShotsOnTarget = homeShotsOnTarget,
            awayShotsOnTarget = awayShotsOnTarget,
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            homeCards = homeCards,
            awayCards = awayCards,
            homeInjuries = homeInjuries,
            awayInjuries = awayInjuries,
            substitutions = substitutions,
            homePenaltyScore = homePenaltyScore,
            awayPenaltyScore = awayPenaltyScore
        )

        return MatchSimulationResult(
            match = finalMatch,
            events = events,
            homeXG = mutableHomeXG,
            awayXG = mutableAwayXG
        )
    }

    /**
     * Determine a realistic shot type based on random distribution.
     */
    private fun determineShotType(random: Random): ShotType {
        val roll = random.nextDouble()
        return when {
            roll < 0.02 -> ShotType.TAP_IN
            roll < 0.10 -> ShotType.CLOSE_RANGE
            roll < 0.40 -> ShotType.PENALTY_AREA
            roll < 0.65 -> ShotType.LONG_RANGE
            roll < 0.78 -> ShotType.VERY_LONG_RANGE
            roll < 0.90 -> ShotType.HEADER
            roll < 0.97 -> ShotType.FREE_KICK
            else -> ShotType.PENALTY
        }
    }

    /**
     * Simulate penalty shootout using kicker vs goalkeeper abilities.
     * Includes increasing pressure modifier as sudden death progresses.
     */
    private fun simulatePenaltyShootout(random: Random): Pair<Int, Int> {
        var homePenalties = 0
        var awayPenalties = 0

        // First 5 rounds
        repeat(5) {
            // Home kicks
            val homeBaseProb = 0.78
            if (random.nextDouble() < homeBaseProb) homePenalties++

            // Away kicks
            val awayBaseProb = 0.75
            if (random.nextDouble() < awayBaseProb) awayPenalties++
        }

        // Sudden death if tied after 5 rounds
        var suddenDeathRound = 6
        while (homePenalties == awayPenalties && suddenDeathRound <= 15) {
            val pressureMultiplier = 1.0 - (suddenDeathRound - 5) * 0.03
            // Home kicks
            if (random.nextDouble() < 0.76 * pressureMultiplier) homePenalties++
            // Away kicks
            if (random.nextDouble() < 0.76 * pressureMultiplier) awayPenalties++
            suddenDeathRound++
        }

        return Pair(homePenalties, awayPenalties)
    }
}