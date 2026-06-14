package com.fifa.simpa.data.api

import com.fifa.simpa.data.model.MachinaHistoricalData
import com.fifa.simpa.data.model.MachinaMatchPrediction
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MachinaApi {

    @GET("api/v1/predictions/{matchId}")
    suspend fun getMatchPrediction(
        @Path("matchId") matchId: String,
        @Query("api_key") apiKey: String
    ): MachinaMatchPrediction

    @GET("api/v1/historical/{season}")
    suspend fun getHistoricalData(
        @Path("season") season: Int = 2026,
        @Query("api_key") apiKey: String
    ): MachinaHistoricalData

    @GET("api/v1/team/{teamId}/metrics")
    suspend fun getTeamMetrics(
        @Path("teamId") teamId: String,
        @Query("api_key") apiKey: String
    ): MachinaHistoricalData
}