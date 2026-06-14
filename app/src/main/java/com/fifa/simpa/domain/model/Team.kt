package com.fifa.simpa.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: String,
    val name: String,
    val shortName: String,
    val countryCode: String,
    val group: String,
    val flagUrl: String = "",
    val logoUrl: String = "",
    val ranking: Int = 0,
    val continent: Continent = Continent.OTHER,
    val isHost: Boolean = false,
    val squad: List<Player> = emptyList(),
    val stats: TeamStats = TeamStats()
)

@Serializable
data class TeamStats(
    val attackStrength: Double = 50.0,
    val midfieldStrength: Double = 50.0,
    val defenseStrength: Double = 50.0,
    val overallStrength: Double = 50.0
)

@Serializable
enum class Continent(val label: String) {
    EUROPE("Europe"),
    SOUTH_AMERICA("South America"),
    NORTH_AMERICA("North America"),
    AFRICA("Africa"),
    ASIA("Asia"),
    OCEANIA("Oceania"),
    OTHER("Other")
}

@Serializable
data class TeamGroupStanding(
    val teamId: String,
    val teamName: String,
    val shortName: String,
    val group: String,
    val played: Int = 0,
    val wins: Int = 0,
    val draws: Int = 0,
    val losses: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val goalDifference: Int = 0,
    val points: Int = 0,
    val fairPlayPoints: Int = 0,
    val form: List<MatchResult> = emptyList(),
    val morale: Float = 0.5f
)

@Serializable
enum class MatchResult(val label: String) {
    WIN("W"),
    DRAW("D"),
    LOSS("L")
}