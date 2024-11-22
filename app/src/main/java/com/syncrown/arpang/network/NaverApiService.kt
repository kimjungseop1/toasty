package com.syncrown.arpang.network

import com.syncrown.arpang.network.model.NaverUserProfileResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface NaverApiService {
    @GET("v1/nid/me")
    suspend fun getUserProfile(
        @Header("Authorization") authorization: String
    ): NaverUserProfileResponse
}

object NaverClient {
    private const val BASE_URL = "https://openapi.naver.com/"

    val instance: NaverApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(NaverApiService::class.java)
    }
}