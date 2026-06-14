package com.fifa.simpa.domain.engine

import com.fifa.simpa.domain.model.Team
import com.fifa.simpa.domain.model.TeamStats
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Poisson Distribution Goal Engine
 *
 * Calculates the exact probability of a team scoring k goals using:
 *   P(k) = (λ^k * e^(-λ)) / k!
 *
 * Where λ (lambda) = expected goals calculated from attacking strength
 * of Team A against the defensive weakness of Team B.
 */
class PoissonGoalEngine {

    /**
     * Calculate expected goals (λ) for a team against an opponent.
     * Uses modified Dixon-Coles approach with weighted parameters.
     */
    fun calculateExpectedGoals(attackTeam: Team, defenseTeam: Team, homeAdvantage: Boolean = true): Double {
        val attackStrength = attackTeam.stats.attackStrength / 50.0
        val defenseStrength = defenseTeam.stats.defenseStrength / 50.0
        val overallQuality = attackTeam.stats.overallStrength / 50.0

        val homeBonus = if (homeAdvantage) {
            // Home/neutral advantage modifier (approx +0.35 goals)
            if (attackTeam.isHost) 0.45 else 0.25
        } else 0.0

        // League average goals per match baseline for international football
        val leagueBaseline = 2.5

        val lambda = leagueBaseline * attackStrength * (1.0 / defenseStrength) * (overallQuality / 1.5) + homeBonus

        // Clamp to realistic range for international football
        return lambda.coerceIn(0.2, 5.0)
    }

    /**
     * Calculate Poisson probability of scoring exactly k goals given lambda.
     * P(k) = (λ^k * e^(-λ)) / k!
     */
    fun probabilityOfKGoals(lambda: Double, k: Int): Double {
        if (k < 0) return 0.0
        return (lambda.pow(k) * exp(-lambda)) / factorial(k)
    }

    /**
     * Generate a random number of goals following Poisson distribution.
     * Uses inverse transform sampling for efficiency.
     */
    fun sampleGoals(lambda: Double, random: Random = Random): Int {
        if (lambda <= 0.0) return 0

        val L = exp(-lambda)
        var p = 1.0
        var k = 0

        do {
            k++
            p *= random.nextDouble()
        } while (p > L && k < 10) // Cap at 10 goals for realism

        return k - 1
    }

    /**
     * Calculate the probability matrix for all possible scorelines.
     * Returns a map of (homeGoals, awayGoals) -> probability.
     */
    fun calculateScoreProbabilityMatrix(
        homeTeam: Team,
        awayTeam: Team,
        homeAdvantage: Boolean = true,
        maxGoals: Int = 6
    ): Map<Pair<Int, Int>, Double> {
        val lambdaHome = calculateExpectedGoals(homeTeam, awayTeam, homeAdvantage)
        val lambdaAway = calculateExpectedGoals(awayTeam, homeTeam, false)

        val matrix = mutableMapOf<Pair<Int, Int>, Double>()

        for (h in 0..maxGoals) {
            for (a in 0..maxGoals) {
                val prob = probabilityOfKGoals(lambdaHome, h) * probabilityOfKGoals(lambdaAway, a)
                matrix[Pair(h, a)] = prob
            }
        }

        return matrix
    }

    /**
     * Calculate match outcome probabilities (H, D, A) from the score matrix.
     */
    fun calculateOutcomeProbabilities(
        homeTeam: Team,
        awayTeam: Team,
        homeAdvantage: Boolean = true,
        maxGoals: Int = 6
    ): Triple<Double, Double, Double> {
        val matrix = calculateScoreProbabilityMatrix(homeTeam, awayTeam, homeAdvantage, maxGoals)

        var homeWin = 0.0
        var draw = 0.0
        var awayWin = 0.0

        for ((score, prob) in matrix) {
            when {
                score.first > score.second -> homeWin += prob
                score.first == score.second -> draw += prob
                else -> awayWin += prob
            }
        }

        // Normalize for floating point rounding
        val total = homeWin + draw + awayWin
        return Triple(homeWin / total, draw / total, awayWin / total)
    }

    /**
     * Calculate expected goals (xG) for a specific shot type/location.
     */
    fun calculateShotXG(
        shotType: ShotType,
        distanceFromGoal: Double, // 0.0 to 40.0 meters
        angleToGoal: Double,       // 0.0 to 90.0 degrees
        isHeader: Boolean = false,
        isThroughBall: Boolean = false
    ): Double {
        var xG = when (shotType) {
            ShotType.TAP_IN -> 0.75
            ShotType.CLOSE_RANGE -> 0.45
            ShotType.PENALTY_AREA -> 0.25
            ShotType.LONG_RANGE -> 0.08
            ShotType.VERY_LONG_RANGE -> 0.03
            ShotType.HEADER -> 0.10
            ShotType.FREE_KICK -> 0.08
            ShotType.PENALTY -> 0.78
        }

        // Distance decay factor
        val distanceModifier = exp(-distanceFromGoal / 20.0)
        xG *= distanceModifier

        // Angle adjustment - narrower angles reduce xG
        val angleModifier = 1.0 - (angleToGoal / 180.0)
        xG *= (0.5 + angleModifier * 0.5)

        // Header modifier
        if (isHeader) xG *= 0.6

        // Through ball bonus
        if (isThroughBall) xG *= 1.2

        return xG.coerceIn(0.01, 0.95)
    }

    private fun factorial(n: Int): Double {
        if (n <= 1) return 1.0
        var result = 1.0
        for (i in 2..n) {
            result *= i
        }
        return result
    }
}

enum class ShotType {
    TAP_IN,
    CLOSE_RANGE,
    PENALTY_AREA,
    LONG_RANGE,
    VERY_LONG_RANGE,
    HEADER,
    FREE_KICK,
    PENALTY
}