package com.fifa.simpa.data.api

import com.fifa.simpa.data.model.SportmonksFixture
import com.fifa.simpa.data.model.SportmonksPrediction
import com.fifa.simpa.data.model.SportmonksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SportmonksApi {

    @GET("v3/football/fixtures")
    suspend fun getFixtures(
        @Query("api_token") apiKey: String,
        @Query("include") include: String = "participants;scores;league;venue;weather",
        @Query("filters[leagueIds]") leagueIds: List<Int>? = null,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100
    ): SportmonksResponse<List<SportmonksFixture>>

    @GET("v3/football/fixtures/{id}")
    suspend fun getFixtureById(
        @Path("id") fixtureId: Int,
        @Query("api_token") apiKey: String,
        @Query("include") include: String = "participants;scores;events;lineups;statistics"
    ): SportmonksResponse<SportmonksFixture>

    @GET("v3/football/fixtures/between/{from}/{to}")
    suspend fun getFixturesByDateRange(
        @Path("from") fromDate: String,
        @Path("to") toDate: String,
        @Query("api_token") apiKey: String,
        @Query("include") include: String = "participants;scores;league",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 50
    ): SportmonksResponse<List<SportmonksFixture>>

    @GET("v3/football/predictions/probabilities/{fixtureId}")
    suspend fun getMatchPredictions(
        @Path("fixtureId") fixtureId: Int,
        @Query("api_token") apiKey: String
    ): SportmonksResponse<SportmonksPrediction>

    @GET("v3/football/leagues/{id}")
    suspend fun getLeagueById(
        @Path("id") leagueId: Int,
        @Query("api_token") apiKey: String
    ): SportmonksResponse<Any>
}