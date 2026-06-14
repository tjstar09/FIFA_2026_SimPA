package com.fifa.simpa.domain.modifier

/**
 * Match-State Momentum Engine
 *
 * Dynamic modifiers applied during runtime. If a team leads by two goals,
 * decrease their attacking intent modifier and scale up the trailing team's
 * possession multiplier.
 */
class MomentumEngine {

    /**
     * Calculate current momentum shift based on match state.
     * Returns a delta value: positive = home momentum, negative = away momentum.
     */
    fun calculateMomentumShift(
        minute: Int,
        homeScore: Int,
        awayScore: Int,
        totalMinutes: Int
    ): Double {
        if (homeScore == awayScore) return 0.0

        val scoreDiff = homeScore - awayScore
        val absoluteDiff = Math.abs(scoreDiff)

        // Momentum is stronger earlier in the match
        val timeFactor = 1.0 - (minute.toDouble() / totalMinutes) * 0.5

        // Larger leads create less momentum shift (teams sit back)
        val leadFactor = when {
            absoluteDiff <= 1 -> 0.8
            absoluteDiff == 2 -> 0.5
            else -> 0.3
        }

        // Goal scored in last 10 minutes creates big momentum
        val recentGoalBonus = if (minute > totalMinutes - 10) 0.3 else 0.0

        if (scoreDiff > 0) {
            return (0.15 * leadFactor * timeFactor + recentGoalBonus) * 1.0
        } else {
            return -(0.15 * leadFactor * timeFactor + recentGoalBonus) * 1.0
        }
    }

    /**
     * Calculate aggression modifier for trailing teams.
     */
    fun calculateAggressionModifier(morale: Float, goalDeficit: Int): Double {
        val baseAggression = 1.0 + (goalDeficit * 0.15)
        val moraleBoost = 1.0 - morale.toDouble() * 0.2
        return (baseAggression + moraleBoost).coerceIn(1.0, 1.5)
    }

    /**
     * Calculate defensive solidity for leading team.
     */
    fun calculateDefensiveSolidity(goalLead: Int): Double {
        return when (goalLead) {
            1 -> 1.05
            2 -> 1.15
            else -> 1.25
        }
    }

    /**
     * Calculate form confidence modifier (rolling average).
     * Takes recent results and produces a confidence multiplier.
     */
    fun calculateFormConfidence(recentForm: List<Boolean>): Double {
        if (recentForm.isEmpty()) return 1.0

        val winRate = recentForm.count { it }.toDouble() / recentForm.size
        return 0.9 + winRate * 0.2
    }
}