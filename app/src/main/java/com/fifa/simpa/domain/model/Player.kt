package com.fifa.simpa.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val name: String,
    val jerseyNumber: Int = 0,
    val position: PlayerPosition = PlayerPosition.SUBSTITUTE,
    val nationality: String = "",
    val age: Int = 25,
    val stamina: Double = 100.0,
    val currentStamina: Double = 100.0,
    val form: Double = 0.5,
    val attributes: PlayerAttributes = PlayerAttributes(),
    val yellowCards: Int = 0,
    val redCards: Int = 0,
    val goals: Int = 0,
    val assists: Int = 0,
    val minutesPlayed: Int = 0,
    val isInjured: Boolean = false,
    val injuryReturnMinute: Int = -1,
    val isSuspended: Boolean = false
)

@Serializable
data class PlayerAttributes(
    val pace: Double = 50.0,
    val shooting: Double = 50.0,
    val passing: Double = 50.0,
    val dribbling: Double = 50.0,
    val defending: Double = 50.0,
    val physical: Double = 50.0,
    val penaltyTaking: Double = 50.0,
    val goalkeeping: Double = 10.0,
    val heading: Double = 50.0,
    val longShots: Double = 50.0,
    val vision: Double = 50.0,
    val composure: Double = 50.0,
    val aggression: Double = 50.0
)

@Serializable
enum class PlayerPosition(val label: String) {
    GK("Goalkeeper"),
    CB("Centre-Back"),
    LB("Left-Back"),
    RB("Right-Back"),
    CDM("Defensive Midfielder"),
    CM("Central Midfielder"),
    CAM("Attacking Midfielder"),
    LM("Left Midfielder"),
    RM("Right Midfielder"),
    LW("Left Winger"),
    RW("Right Winger"),
    CF("Centre-Forward"),
    ST("Striker"),
    SUBSTITUTE("Substitute")
}

data class PlayerFatigueState(
    val playerId: String,
    val staminaPercentage: Double,
    val passAccuracyModifier: Double = 1.0,
    val tackleSuccessModifier: Double = 1.0,
    val speedModifier: Double = 1.0
)