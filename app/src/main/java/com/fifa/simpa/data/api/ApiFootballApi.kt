package com.fifa.simpa.data.api

import com.fifa.simpa.data.model.ApiFootballFixture
import com.fifa.simpa.data.model.ApiFootballResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiFootballApi {

    @GET("v3/fixtures")
    @Headers("Accept: application/json")
    suspend fun getFixtures(
        @Query("league") leagueId: Int? = null,
        @Query("season") season: Int = 2026,
        @Query("date") date: String? = null,
        @Query("status") status: String? = null,
        @Query("team") teamId: Int? = null,
        @Query("last") last: Int? = null,
        @Query("next") next: Int? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("page") page: Int = 1
    ): ApiFootballResponse<List<ApiFootballFixture>>

    @GET("v3/fixtures/{id}")
    suspend fun getFixtureById(
        @Path("id") fixtureId: Int
    ): ApiFootballResponse<List<ApiFootballFixture>>

    @GET("v3/teams")
    suspend fun getTeams(
        @Query("league") leagueId: Int? = null,
        @Query("season") season: Int = 2026,
        @Query("id") teamId: Int? = null,
        @Query("search") search: String? = null
    ): ApiFootballResponse<List<Any>>

    @GET("v3/standings")
    suspend fun getStandings(
        @Query("league") leagueId: Int,
        @Query("season") season: Int = 2026
    ): ApiFootballResponse<List<Any>>

    @GET("v3/teams/statistics")
    suspend fun getTeamStatistics(
        @Query("league") leagueId: Int,
        @Query("season") season: Int = 2026,
        @Query("team") teamId: Int
    ): ApiFootballResponse<List<Any>>
}