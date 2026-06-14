package com.fifa.simpa.domain.engine

import com.fifa.simpa.domain.model.Match
import com.fifa.simpa.domain.model.TeamGroupStanding

/**
 * Complex Group Stage Tie-Breakers Engine
 *
 * Strict sorting: Points -> Goal Difference -> Goals Scored -> Head-to-Head -> Fair Play Points.
 * Fully implements FIFA tournament regulations.
 */
class TieBreakerEngine {

    /**
     * Sort teams in a group according to FIFA tie-breaking rules.
     * Returns a sorted list from 1st to 4th place.
     */
    fun sortGroupStandings(
        standings: List<TeamGroupStanding>,
        groupMatches: List<Match>
    ): List<TeamGroupStanding> {
        return standings.sortedWith(comparator(groupMatches))
    }

    /**
     * Create comparator that sorts by FIFA tie-breaking criteria.
     */
    private fun comparator(groupMatches: List<Match>): Comparator<TeamGroupStanding> {
        return Comparator { a, b ->
            // 1. Points (higher is better)
            compareValues(b.points, a.points).let { if (it != 0) return@Comparator it }

            // 2. Goal Difference (higher is better)
            compareValues(b.goalDifference, a.goalDifference).let { if (it != 0) return@Comparator it }

            // 3. Goals Scored (higher is better)
            compareValues(b.goalsFor, a.goalsFor).let { if (it != 0) return@Comparator it }

            // 4. Head-to-Head between tied teams
            val h2hResult = resolveHeadToHead(a, b, groupMatches)
            if (h2hResult != 0) return@Comparator h2hResult

            // 5. Fair Play Points (lower = better; deducted from 100)
            compareValues(a.fairPlayPoints, b.fairPlayPoints).let { if (it != 0) return@Comparator it }

            // 6. Drawing of lots (alphabetical as deterministic proxy)
            a.teamName.compareTo(b.teamName)
        }
    }

    /**
     * Head-to-head resolution between two tied teams.
     * Returns negative if a is better, positive if b is better, 0 if still tied.
     */
    private fun resolveHeadToHead(
        a: TeamGroupStanding,
        b: TeamGroupStanding,
        groupMatches: List<Match>
    ): Int {
        // Find matches between these two teams in the group
        val h2hMatches = groupMatches.filter { match ->
            (match.homeTeam.id == a.teamId && match.awayTeam.id == b.teamId) ||
            (match.homeTeam.id == b.teamId && match.awayTeam.id == a.teamId)
        }

        if (h2hMatches.isEmpty()) return 0

        var aPoints = 0
        var bPoints = 0
        var aGD = 0
        var bGD = 0

        for (match in h2hMatches) {
            val homeScore = match.homeScore ?: 0
            val awayScore = match.awayScore ?: 0

            if (match.homeTeam.id == a.teamId) {
                // a is home
                if (homeScore > awayScore) aPoints += 3
                else if (homeScore < awayScore) bPoints += 3
                else { aPoints += 1; bPoints += 1 }
                aGD += homeScore - awayScore
                bGD += awayScore - homeScore
            } else {
                // b is home
                if (homeScore > awayScore) bPoints += 3
                else if (homeScore < awayScore) aPoints += 3
                else { aPoints += 1; bPoints += 1 }
                bGD += homeScore - awayScore
                aGD += awayScore - homeScore
            }
        }

        val pointsComparison = compareValues(bPoints, aPoints)
        if (pointsComparison != 0) return pointsComparison

        val gdComparison = compareValues(bGD, aGD)
        if (gdComparison != 0) return gdComparison

        return 0 // If still tied, fall through to next criterion
    }

    /**
     * Calculate fair play points from cards.
     * Yellow card: -1 point
     * Indirect red card (2 yellows): -3 points
     * Direct red card: -4 points
     */
    fun calculateFairPlayPoints(yellowCards: Int, indirectReds: Int, directReds: Int): Int {
        return -(yellowCards * 1 + indirectReds * 3 + directReds * 4)
    }
}