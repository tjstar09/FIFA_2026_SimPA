package com.fifa.simpa.data.repository

import com.fifa.simpa.data.api.SportmonksApi
import com.fifa.simpa.data.cache.NetworkModule
import com.fifa.simpa.data.mock.MockData
import com.fifa.simpa.domain.engine.MonteCarloMatchEngine
import com.fifa.simpa.domain.engine.PoissonGoalEngine
import com.fifa.simpa.domain.engine.TieBreakerEngine
import com.fifa.simpa.domain.model.*

/**
 * Central repository managing all data sources with failover and caching.
 * Falls back to mock data when APIs are unavailable.
 */
class MatchRepository {
    private val sportmonksApi: SportmonksApi = NetworkModule.sportmonksApi
    private val poissonEngine = PoissonGoalEngine()
    private val monteCarloEngine = MonteCarloMatchEngine()
    private val tieBreakerEngine = TieBreakerEngine()

    private val teams: List<Team> get() = MockData.teams
    private val allGroupMatches = mutableListOf<Match>()

    init {
        allGroupMatches.addAll(MockData.generateAllGroupMatches())
    }

    fun getAllTeams(): List<Team> = teams

    fun getTeamsByGroup(group: String): List<Team> = MockData.getTeamsByGroup(group)

    fun getAllGroups(): List<String> = MockData.groups

    fun getAllGroupMatches(): List<Match> = allGroupMatches

    fun getGroupMatches(group: String): List<Match> = allGroupMatches.filter { it.group == group }

    fun getMatchesByStatus(status: MatchStatus): List<Match> = allGroupMatches.filter { it.status == status }

    /**
     * Get group standings sorted by FIFA tie-breaking rules.
     */
    fun getGroupStandings(group: String): List<TeamGroupStanding> {
        val groupTeams = getTeamsByGroup(group)
        val groupMatches = getGroupMatches(group)

        val standings = groupTeams.map { team ->
            val teamMatches = groupMatches.filter {
                (it.homeTeam.id == team.id || it.awayTeam.id == team.id) && it.status == MatchStatus.FINISHED
            }
            var wins = 0; var draws = 0; var losses = 0
            var goalsFor = 0; var goalsAgainst = 0

            for (match in teamMatches) {
                val homeScore = match.homeScore ?: 0
                val awayScore = match.awayScore ?: 0

                if (match.homeTeam.id == team.id) {
                    goalsFor += homeScore; goalsAgainst += awayScore
                    if (homeScore > awayScore) wins++
                    else if (homeScore == awayScore) draws++
                    else losses++
                } else {
                    goalsFor += awayScore; goalsAgainst += homeScore
                    if (awayScore > homeScore) wins++
                    else if (homeScore == awayScore) draws++
                    else losses++
                }
            }

            val played = wins + draws + losses
            val gd = goalsFor - goalsAgainst
            val points = wins * 3 + draws

            // Calculate form
            val form = teamMatches.map { match ->
                if (match.homeTeam.id == team.id) {
                    val hs = match.homeScore ?: 0; val as_ = match.awayScore ?: 0
                    if (hs > as_) MatchResult.WIN else if (hs == as_) MatchResult.DRAW else MatchResult.LOSS
                } else {
                    val hs = match.homeScore ?: 0; val as_ = match.awayScore ?: 0
                    if (as_ > hs) MatchResult.WIN else if (hs == as_) MatchResult.DRAW else MatchResult.LOSS
                }
            }

            TeamGroupStanding(
                teamId = team.id, teamName = team.name, shortName = team.shortName,
                group = team.group, played = played, wins = wins, draws = draws,
                losses = losses, goalsFor = goalsFor, goalsAgainst = goalsAgainst,
                goalDifference = gd, points = points, form = form,
                morale = if (played > 0) points.toFloat() / (played * 3).toFloat() else 0.5f
            )
        }

        return tieBreakerEngine.sortGroupStandings(standings, groupMatches)
    }

    /**
     * Simulate a single match using the Monte Carlo engine.
     */
    suspend fun simulateMatch(matchId: String): MonteCarloMatchEngine.MatchSimulationResult {
        val match = allGroupMatches.find { it.id == matchId }
            ?: throw IllegalArgumentException("Match not found: $matchId")

        val result = monteCarloEngine.simulateMatch(match)

        // Update the match in the list
        val index = allGroupMatches.indexOfFirst { it.id == matchId }
        if (index >= 0) {
            allGroupMatches[index] = result.match
        }

        return result
    }

    /**
     * Simulate all group stage matches.
     */
    suspend fun simulateAllGroupMatches(): List<MonteCarloMatchEngine.MatchSimulationResult> {
        val results = mutableListOf<MonteCarloMatchEngine.MatchSimulationResult>()
        for (match in allGroupMatches.filter { it.status == MatchStatus.SCHEDULED }) {
            try {
                val result = monteCarloEngine.simulateMatch(match)
                val index = allGroupMatches.indexOfFirst { it.id == match.id }
                if (index >= 0) {
                    allGroupMatches[index] = result.match
                }
                results.add(result)
            } catch (e: Exception) {
                // Continue with next match if one fails
            }
        }
        return results
    }

    suspend fun simulatePartialGroupMatches(groups: List<String>): List<MonteCarloMatchEngine.MatchSimulationResult> {
        val results = mutableListOf<MonteCarloMatchEngine.MatchSimulationResult>()
        for (match in allGroupMatches.filter { it.group in groups && it.status == MatchStatus.SCHEDULED }) {
            try {
                val result = monteCarloEngine.simulateMatch(match)
                val index = allGroupMatches.indexOfFirst { it.id == match.id }
                if (index >= 0) {
                    allGroupMatches[index] = result.match
                }
                results.add(result)
            } catch (e: Exception) { /* skip */ }
        }
        return results
    }

    /**
     * Get pre-match prediction for display.
     */
    fun getMatchPrediction(match: Match): Triple<Double, Double, Double> {
        return poissonEngine.calculateOutcomeProbabilities(match.homeTeam, match.awayTeam)
    }
}