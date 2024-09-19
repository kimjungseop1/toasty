package com.syncrown.toasty.network

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.syncrown.toasty.AppDataPref
import com.syncrown.toasty.network.model.RequestCheckMember
import com.syncrown.toasty.network.model.RequestJoinDto
import com.syncrown.toasty.network.model.ResponseCheckMember
import com.syncrown.toasty.network.model.ResponseJoinDto
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ToastyRepository {
    companion object {
        private const val BASE_URL = "http://192.168.0.4:8090"
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

        toastyInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ToastyInterface::class.java)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //TODO 회원여부 확인
    val checkMemberLiveDataRepository: MutableLiveData<NetworkResult<ResponseCheckMember>> =
        MutableLiveData()

    //TODO 가입신청
    val joinLiveDataRepository: MutableLiveData<NetworkResult<ResponseJoinDto>> = MutableLiveData()


    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun requestCheckMember(requestCheckMember: RequestCheckMember) {
        toastyInterface.postCheckMember(requestCheckMember).enqueue(object : Callback<ResponseCheckMember> {
            override fun onResponse(
                call: Call<ResponseCheckMember>,
                response: Response<ResponseCheckMember>
            ) {
                checkMemberLiveDataRepository.postValue(NetworkResult.Success(response.body()))
            }

            override fun onFailure(call: Call<ResponseCheckMember>, t: Throwable) {
                checkMemberLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    fun requestJoin(requestJoinDto: RequestJoinDto) {
        toastyInterface.postJoin(requestJoinDto).enqueue(object :
            Callback<ResponseJoinDto> {

            override fun onResponse(
                call: Call<ResponseJoinDto>,
                response: Response<ResponseJoinDto>
            ) {
                if (response.code() == 200) {
                    joinLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    joinLiveDataRepository.postValue(
                        NetworkResult.Error(
                            response.message().toString()
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ResponseJoinDto>, t: Throwable) {
                joinLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }
}