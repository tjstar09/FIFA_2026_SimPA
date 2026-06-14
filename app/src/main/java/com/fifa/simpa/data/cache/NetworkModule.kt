package com.fifa.simpa.data.cache

import com.fifa.simpa.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import com.fifa.simpa.data.api.ApiFootballApi
import com.fifa.simpa.data.api.MachinaApi
import com.fifa.simpa.data.api.SportmonksApi
import okhttp3.MediaType.Companion.toMediaType
import java.util.concurrent.TimeUnit

object NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = false
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "FIFA2026SimPA/1.0")
            .build()
        chain.proceed(newRequest)
    }

    private val apiFootballInterceptor = Interceptor { chain ->
        val original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.API_FOOTBALL_KEY)
            .build()
        chain.proceed(original.newBuilder().url(url).build())
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .cache(okhttp3.Cache(
            java.io.File(System.getProperty("java.io.tmpdir"), "simpa-cache"),
            10L * 1024 * 1024 // 10 MB cache
        ))
        .build()

    private val apiFootballClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(apiFootballInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val contentType = "application/json".toMediaType()
    private val converterFactory = json.asConverterFactory(contentType)

    // Base URLs - configurable per environment
    private const val SPORTMONKS_BASE_URL = "https://api.sportmonks.com/"
    private const val API_FOOTBALL_BASE_URL = "https://v3.football.api-sports.io/"
    private const val MACHINA_BASE_URL = "https://api.machina.gg/"

    val sportmonksApi: SportmonksApi by lazy {
        Retrofit.Builder()
            .baseUrl(SPORTMONKS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
            .create(SportmonksApi::class.java)
    }

    val apiFootballApi: ApiFootballApi by lazy {
        Retrofit.Builder()
            .baseUrl(API_FOOTBALL_BASE_URL)
            .client(apiFootballClient)
            .addConverterFactory(converterFactory)
            .build()
            .create(ApiFootballApi::class.java)
    }

    val machinaApi: MachinaApi by lazy {
        Retrofit.Builder()
            .baseUrl(MACHINA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
            .create(MachinaApi::class.java)
    }
}