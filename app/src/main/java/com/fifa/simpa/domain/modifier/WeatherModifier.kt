package com.fifa.simpa.domain.modifier

import com.fifa.simpa.domain.model.WeatherCondition

/**
 * Weather Variables Engine
 *
 * - Rain: Increases passing turnover metrics and scales up long-distance shooting success.
 * - Heat/Humidity: Accelerates the stamina decay curve slope.
 * - Wind: Affects shot accuracy and pass completion.
 * - Snow: Slows the game, reduces all metrics slightly.
 */
class WeatherModifier {

    data class WeatherEffects(
        val possessionModifier: Double,
        val shotSuccessModifier: Double,
        val passCompletionModifier: Double,
        val fatigueAcceleration: Double
    )

    fun applyWeatherEffects(weather: WeatherCondition): WeatherEffects {
        return when (weather) {
            WeatherCondition.NORMAL -> WeatherEffects(1.0, 1.0, 1.0, 1.0)
            WeatherCondition.RAIN -> WeatherEffects(
                possessionModifier = 0.95,
                shotSuccessModifier = 1.12,
                passCompletionModifier = 0.88,
                fatigueAcceleration = 1.05
            )
            WeatherCondition.HEAT -> WeatherEffects(
                possessionModifier = 0.98,
                shotSuccessModifier = 0.95,
                passCompletionModifier = 0.92,
                fatigueAcceleration = 1.25
            )
            WeatherCondition.WIND -> WeatherEffects(
                possessionModifier = 0.97,
                shotSuccessModifier = 0.88,
                passCompletionModifier = 0.90,
                fatigueAcceleration = 1.0
            )
            WeatherCondition.SNOW -> WeatherEffects(
                possessionModifier = 0.90,
                shotSuccessModifier = 0.92,
                passCompletionModifier = 0.80,
                fatigueAcceleration = 1.15
            )
        }
    }
}