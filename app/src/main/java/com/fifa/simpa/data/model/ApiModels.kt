package com.fifa.simpa.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ===== Sportmonks API Models =====

@Serializable
data class SportmonksResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val pagination: SportmonksPagination? = null
)

@Serializable
data class SportmonksPagination(
    val count: Int = 0,
    val perPage: Int = 100,
    val currentPage: Int = 1,
    val nextPage: Int? = null,
    val hasMore: Boolean = false
)

@Serializable
data class SportmonksFixture(
    val id: Int = 0,
    val name: String = "",
    @SerialName("starting_at") val startingAt: String = "",
    @SerialName("result_info") val resultInfo: String? = null,
    val status: String? = null,
    val league: SportmonksLeague? = null,
    val participants: List<SportmonksParticipant>? = null,
    val scores: List<SportmonksScore>? = null,
    val venue: String? = null,
    val weather: SportmonksWeather? = null
)

@Serializable
data class SportmonksLeague(
    val id: Int = 0,
    val name: String = "",
    @SerialName("type") val leagueType: String? = null
)

@Serializable
data class SportmonksParticipant(
    val id: Int = 0,
    val name: String = "",
    @SerialName("short_code") val shortCode: String? = null,
    @SerialName("image_path") val imagePath: String? = null,
    val flag: String? = null
)

@Serializable
data class SportmonksScore(
    val id: Int = 0,
    val score: Int? = null,
    val description: String? = null,
    @SerialName("type_id") val typeId: Int? = null
)

@Serializable
data class SportmonksWeather(
    val temperature: Double? = null,
    val humidity: Int? = null,
    val description: String? = null
)

@Serializable
data class SportmonksPrediction(
    val id: Int = 0,
    val fixtureId: Int = 0,
    val prediction: String? = null,
    val homeXG: Double? = null,
    val awayXG: Double? = null,
    val probabilities: SportmonksProbabilities? = null
)

@Serializable
data class SportmonksProbabilities(
    val home: Double? = null,
    val draw: Double? = null,
    val away: Double? = null
)

// ===== API-Football Models =====

@Serializable
data class ApiFootballResponse<T>(
    val get: String = "",
    val parameters: Map<String, String>? = null,
    val errors: List<String>? = null,
    val results: Int = 0,
    val paging: ApiFootballPaging? = null,
    val response: T? = null
)

@Serializable
data class ApiFootballPaging(
    val current: Int = 1,
    val total: Int = 1
)

@Serializable
data class ApiFootballFixture(
    val fixture: ApiFixtureDetails? = null,
    val league: ApiLeagueInfo? = null,
    val teams: ApiTeamsInfo? = null,
    val goals: ApiGoalsInfo? = null,
    val score: ApiScoreInfo? = null,
    val events: List<ApiEvent>? = null,
    val statistics: List<ApiTeamStatistics>? = null,
    val lineups: List<ApiLineup>? = null
)

@Serializable
data class ApiFixtureDetails(
    val id: Int = 0,
    val date: String? = null,
    val status: ApiStatus? = null,
    val venue: ApiVenue? = null
)

@Serializable
data class ApiStatus(
    val long: String? = null,
    val short: String? = null,
    val elapsed: Int? = null
)

@Serializable
data class ApiVenue(
    val name: String? = null,
    val city: String? = null
)

@Serializable
data class ApiLeagueInfo(
    val id: Int = 0,
    val name: String? = null,
    val season: Int? = null,
    val round: String? = null
)

@Serializable
data class ApiTeamsInfo(
    val home: ApiTeamInfo? = null,
    val away: ApiTeamInfo? = null
)

@Serializable
data class ApiTeamInfo(
    val id: Int = 0,
    val name: String? = null,
    val logo: String? = null,
    val winner: Boolean? = null
)

@Serializable
data class ApiGoalsInfo(
    val home: Int? = null,
    val away: Int? = null
)

@Serializable
data class ApiScoreInfo(
    val halftime: ApiGoalsInfo? = null,
    val fulltime: ApiGoalsInfo? = null,
    val extratime: ApiGoalsInfo? = null,
    val penalty: ApiGoalsInfo? = null
)

@Serializable
data class ApiEvent(
    val time: ApiEventTime? = null,
    val team: ApiEventTeam? = null,
    val player: ApiEventPlayer? = null,
    val assist: ApiEventAssist? = null,
    val type: String? = null,
    val detail: String? = null,
    val comments: String? = null
)

@Serializable
data class ApiEventTime(
    val elapsed: Int? = null,
    val extra: Int? = null
)

@Serializable
data class ApiEventTeam(
    val id: Int = 0,
    val name: String? = null,
    val logo: String? = null
)

@Serializable
data class ApiEventPlayer(
    val id: Int = 0,
    val name: String? = null
)

@Serializable
data class ApiEventAssist(
    val id: Int? = null,
    val name: String? = null
)

@Serializable
data class ApiTeamStatistics(
    val team: ApiTeamInfo? = null,
    val statistics: List<ApiStatisticValue>? = null
)

@Serializable
data class ApiStatisticValue(
    val type: String? = null,
    val value: kotlinx.serialization.json.JsonElement? = null
)

@Serializable
data class ApiLineup(
    val team: ApiTeamInfo? = null,
    val formation: String? = null,
    val startXI: List<ApiPlayerLineup>? = null,
    val substitutes: List<ApiPlayerLineup>? = null
)

@Serializable
data class ApiPlayerLineup(
    val player: ApiPlayerInfo? = null
)

@Serializable
data class ApiPlayerInfo(
    val id: Int = 0,
    val name: String? = null,
    val number: Int? = null,
    val pos: String? = null,
    val grid: String? = null
)

// ===== Machina Sports Models =====

@Serializable
data class MachinaMatchPrediction(
    val matchId: String = "",
    val homeTeam: String = "",
    val awayTeam: String = "",
    val predictedHomeGoals: Double = 0.0,
    val predictedAwayGoals: Double = 0.0,
    val homeWinProb: Double = 0.0,
    val drawProb: Double = 0.0,
    val awayWinProb: Double = 0.0,
    val over25Prob: Double = 0.0,
    val bttsProb: Double = 0.0,
    val confidence: Double = 0.0
)

@Serializable
data class MachinaHistoricalData(
    val season: Int = 0,
    val teams: Map<String, MachinaTeamMetrics>? = null
)

@Serializable
data class MachinaTeamMetrics(
    val attackingStrength: Double = 0.0,
    val defensiveStrength: Double = 0.0,
    val homeAdvantage: Double = 0.0,
    val recentForm: List<Double>? = null,
    val xGPerMatch: Double = 0.0,
    val xGAPerMatch: Double = 0.0
)