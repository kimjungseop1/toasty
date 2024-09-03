package com.syncrown.toasty.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.syncrown.toasty.AppDataPref
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ToastyRepository {
    companion object {
        private const val BASE_URL = ""
    }

    private var toastyInterface: ToastyInterface

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val interceptorAuth = Interceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${AppDataPref.AccessToken}").build()
            chain.proceed(request)
        }

        val client: okhttp3.OkHttpClient = okhttp3.OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(interceptorAuth)
            .retryOnConnectionFailure(true)
            .build()

        val gson: Gson = GsonBuilder().setLenient().create()

        toastyInterface = Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ToastyInterface::class.java)
    }
}