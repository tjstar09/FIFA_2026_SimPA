package com.fifa.simpa.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val id: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val group: String = "",
    val stage: MatchStage = MatchStage.GROUP,
    val date: Long = 0L,
    val venue: String = "",
    val status: MatchStatus = MatchStatus.SCHEDULED,
    val homeScore: Int? = null,
    val awayScore: Int? = null,
    val minute: Int = 0,
    val homeXG: Double = 0.0,
    val awayXG: Double = 0.0,
    val homePossession: Int = 50,
    val awayPossession: Int = 50,
    val homeShots: Int = 0,
    val awayShots: Int = 0,
    val homeShotsOnTarget: Int = 0,
    val awayShotsOnTarget: Int = 0,
    val homeCards: List<CardEvent> = emptyList(),
    val awayCards: List<CardEvent> = emptyList(),
    val homeGoals: List<GoalEvent> = emptyList(),
    val awayGoals: List<GoalEvent> = emptyList(),
    val homeInjuries: List<InjuryEvent> = emptyList(),
    val awayInjuries: List<InjuryEvent> = emptyList(),
    val substitutions: List<SubstitutionEvent> = emptyList(),
    val weather: WeatherCondition = WeatherCondition.NORMAL,
    val refereeStrictness: Double = 1.0,
    val homePenaltyScore: Int? = null,
    val awayPenaltyScore: Int? = null
)

@Serializable
enum class MatchStatus {
    SCHEDULED,
    LIVE,
    HALF_TIME,
    FULL_TIME,
    EXTRA_TIME,
    PENALTIES,
    FINISHED,
    POSTPONED,
    CANCELLED
}

@Serializable
enum class MatchStage(val label: String) {
    GROUP("Group Stage"),
    ROUND_OF_16("Round of 16"),
    QUARTER_FINAL("Quarter-final"),
    SEMI_FINAL("Semi-final"),
    THIRD_PLACE("Third Place"),
    FINAL("Final")
}

@Serializable
enum class WeatherCondition(val label: String) {
    NORMAL("Normal"),
    RAIN("Rain"),
    HEAT("Heat / High Humidity"),
    WIND("Strong Wind"),
    SNOW("Snow")
}

@Serializable
data class GoalEvent(
    val playerId: String,
    val playerName: String,
    val minute: Int,
    val xG: Double = 0.0,
    val isPenalty: Boolean = false,
    val isOwnGoal: Boolean = false,
    val isHeader: Boolean = false,
    val assistPlayerId: String? = null,
    val assistPlayerName: String? = null
)

@Serializable
data class CardEvent(
    val playerId: String,
    val playerName: String,
    val minute: Int,
    val isYellow: Boolean = true,
    val isSecondYellow: Boolean = false
)

@Serializable
data class InjuryEvent(
    val playerId: String,
    val playerName: String,
    val minute: Int,
    val severity: InjurySeverity = InjurySeverity.MINOR,
    val outPosition: PlayerPosition? = null
)

@Serializable
enum class InjurySeverity(val label: String) {
    MINOR("Minor"),
    MODERATE("Moderate"),
    SEVERE("Severe"),
    TOURNAMENT_ENDING("Tournament-ending")
}

@Serializable
data class SubstitutionEvent(
    val minute: Int,
    val playerOffId: String,
    val playerOffName: String,
    val playerOnId: String,
    val playerOnName: String,
    val teamId: String
)

@Serializable
data class MatchLineup(
    val teamId: String,
    val formation: String = "4-4-2",
    val startingXI: List<Player> = emptyList(),
    val substitutes: List<Player> = emptyList()
)

@Serializable
data class SimulatedEvent(
    val minute: Int,
    val eventType: EventType,
    val description: String,
    val teamId: String? = null,
    val playerName: String? = null,
    val impact: Double = 0.0
)

@Serializable
enum class EventType(val label: String) {
    KICK_OFF("Kick-off"),
    POSSESSION("Possession"),
    PASS("Pass"),
    TACKLE("Tackle"),
    FOUL("Foul"),
    SHOT("Shot"),
    GOAL("Goal!"),
    SAVE("Save"),
    CORNER("Corner"),
    FREE_KICK("Free Kick"),
    PENALTY("Penalty"),
    CARD("Card"),
    INJURY("Injury"),
    SUBSTITUTION("Substitution"),
    HALF_TIME("Half Time"),
    FULL_TIME("Full Time"),
    EXTRA_TIME_START("Extra Time Start"),
    PENALTY_SHOOTOUT("Penalty Shootout"),
    VAR_CHECK("VAR Check")
}