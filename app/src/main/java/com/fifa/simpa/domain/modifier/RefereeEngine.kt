package com.fifa.simpa.domain.modifier

/**
 * Referee Strictness Index
 *
 * Reads referee meta-data and maps a hidden strictness coefficient (1.0 to 2.0)
 * that multiplies foul probabilities, cards, and penalty kicks.
 */
class RefereeEngine {

    /**
     * Get the foul probability multiplier based on referee strictness.
     * Strictness 1.0 = lenient, 2.0 = strict (card-happy).
     */
    fun getFoulProbabilityMultiplier(strictness: Double): Double {
        return strictness.coerceIn(1.0, 2.0)
    }

    /**
     * Calculate probability of a foul being given based on strictness.
     */
    fun getFoulCallProbability(strictness: Double): Double {
        val baseProb = 0.15
        return baseProb * strictness
    }

    /**
     * Calculate probability of a yellow card for a given foul.
     */
    fun getYellowCardProbability(strictness: Double, foulSeverity: Double = 0.5): Double {
        val baseProb = 0.10 * foulSeverity
        return (baseProb * strictness).coerceIn(0.05, 0.35)
    }

    /**
     * Calculate probability of a red card for a given foul.
     */
    fun getRedCardProbability(strictness: Double, foulSeverity: Double = 0.5): Double {
        val baseProb = 0.02 * foulSeverity
        return (baseProb * strictness).coerceIn(0.01, 0.08)
    }

    /**
     * Calculate probability of a penalty being awarded in a match.
     */
    fun getPenaltyProbability(strictness: Double): Double {
        val baseProb = 0.012 // ~1.2% per minute
        return baseProb * strictness
    }
}