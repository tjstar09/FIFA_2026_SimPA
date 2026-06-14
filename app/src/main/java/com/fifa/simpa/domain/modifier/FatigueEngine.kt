package com.fifa.simpa.domain.modifier

import com.fifa.simpa.domain.model.Player
import com.fifa.simpa.domain.model.PlayerPosition
import kotlin.math.pow

/**
 * Fatigue & Stamina Decay Engine
 *
 * Applies a non-linear decay curve to player attributes throughout the match.
 * Midfielders drain faster than defenders. Below a 60% stamina threshold,
 * decrease pass accuracy and tackle success metrics by a calculated penalty.
 */
class FatigueEngine {

    // Base decay rate per minute for different positions
    private val decayRates = mapOf(
        PlayerPosition.GK to 0.15,
        PlayerPosition.CB to 0.35,
        PlayerPosition.LB to 0.50,
        PlayerPosition.RB to 0.50,
        PlayerPosition.CDM to 0.65,
        PlayerPosition.CM to 0.70,
        PlayerPosition.CAM to 0.60,
        PlayerPosition.LM to 0.60,
        PlayerPosition.RM to 0.60,
        PlayerPosition.LW to 0.55,
        PlayerPosition.RW to 0.55,
        PlayerPosition.CF to 0.50,
        PlayerPosition.ST to 0.50,
        PlayerPosition.SUBSTITUTE to 0.0
    )

    /**
     * Calculate fatigue value at a given minute (0.0 = fresh, 1.0 = exhausted).
     * Uses a non-linear exponential curve that accelerates in the last 30 minutes.
     */
    fun calculateFatigueAtMinute(
        minute: Int,
        totalMinutes: Int,
        accelerationFactor: Double = 1.0
    ): Double {
        val progress = minute.toDouble() / totalMinutes

        // Non-linear decay: quadratic curve that accelerates
        val baseFatigue = progress.pow(1.5)

        // Sprint to the end - extra fatigue in final 15 minutes
        val endgameBonus = if (minute > totalMinutes - 15) {
            ((minute - (totalMinutes - 15)) / 15.0) * 0.2
        } else 0.0

        return (baseFatigue + endgameBonus).coerceIn(0.0, 1.0) * accelerationFactor
    }

    /**
     * Get midfield fatigue modifier for possession calculations.
     * Returns a value that reduces midfield dominance as match progresses.
     */
    fun getMidfieldFatigueModifier(minute: Int): Double {
        val fatigue = calculateFatigueAtMinute(minute, 90)
        return 1.0 - fatigue * 0.25
    }

    /**
     * Calculate stamina remaining for a specific player based on position.
     */
    fun calculatePlayerStamina(
        player: Player,
        minute: Int,
        totalMinutes: Int,
        accelerationFactor: Double = 1.0
    ): Double {
        val baseDecay = decayRates[player.position] ?: 0.5
        val progress = minute.toDouble() / totalMinutes
        val decayAmount = baseDecay * progress * accelerationFactor

        val stamina = player.stamina - (decayAmount * 100.0)

        return stamina.coerceIn(0.0, player.stamina)
    }

    /**
     * Calculate performance penalty when stamina drops below threshold.
     * Below 60% stamina, accuracy/success metrics decrease.
     */
    fun getFatiguePenalty(stamina: Double): Double {
        if (stamina >= 60.0) return 1.0 // No penalty above 60%

        // Linear penalty from 1.0 at 60% to 0.7 at 0%
        val penalty = 1.0 - (60.0 - stamina) / 60.0 * 0.3
        return penalty.coerceIn(0.7, 1.0)
    }

    /**
     * Calculate pass accuracy modifier based on fatigue.
     */
    fun getPassAccuracyModifier(fatigue: Double): Double {
        return 1.0 - fatigue * 0.25
    }

    /**
     * Calculate tackle success modifier based on fatigue.
     */
    fun getTackleSuccessModifier(fatigue: Double): Double {
        return 1.0 - fatigue * 0.2
    }

    /**
     * Calculate speed/pace modifier based on fatigue.
     */
    fun getSpeedModifier(fatigue: Double): Double {
        return 1.0 - fatigue * 0.3
    }
}