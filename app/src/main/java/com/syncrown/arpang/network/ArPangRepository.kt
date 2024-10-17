package com.syncrown.arpang.network

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArPangRepository {
    companion object {
        private const val BASE_URL = "http://192.168.0.4:8090"
    }

    private var arPangInterface: ArPangInterface

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

        arPangInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ArPangInterface::class.java)
    }

    /***********************************************************************************************
     * 각 뷰모델에서 사용하기 위한 라이브데이터
     **********************************************************************************************/
    //TODO 001. 회원여부 확인
    val checkMemberLiveDataRepository: MutableLiveData<NetworkResult<ResponseCheckMember>> =
        MutableLiveData()

    //TODO 002. 가입신청
    val joinLiveDataRepository: MutableLiveData<NetworkResult<ResponseJoinDto>> = MutableLiveData()

    //TODO 003. 로그인
    val loginLiveDataRepository: MutableLiveData<NetworkResult<ResponseLoginDto>> =
        MutableLiveData()

    //TODO 004.

    /***********************************************************************************************
     * API response를 라이브데이터에 추가
     **********************************************************************************************/
    //TODO 001. 회원여부 확인
    fun requestCheckMember(requestCheckMember: RequestCheckMember) {
        arPangInterface.postCheckMember(requestCheckMember)
            .enqueue(object : Callback<ResponseCheckMember> {
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

    //TODO 002. 가입신청
    fun requestJoin(requestJoinDto: RequestJoinDto) {
        arPangInterface.postJoin(requestJoinDto).enqueue(object :
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

    //TODO 003. 로그인
    fun requestLogin(requestLoginDto: RequestLoginDto) {
        arPangInterface.postLogin(requestLoginDto).enqueue(object : Callback<ResponseLoginDto> {
            override fun onResponse(
                call: Call<ResponseLoginDto>,
                response: Response<ResponseLoginDto>
            ) {
                if (response.code() == 200) {
                    loginLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    loginLiveDataRepository.postValue(
                        NetworkResult.Error(
                            response.message().toString()
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ResponseLoginDto>, t: Throwable) {
                loginLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }
}