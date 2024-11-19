package com.syncrown.arpang.network

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestCheckNickNameDto
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.RequestUpdateProfileDto
import com.syncrown.arpang.network.model.RequestUserTokenDelDto
import com.syncrown.arpang.network.model.RequestUserTokenRegDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseCheckNickNameDto
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.network.model.ResponseUpdateProfileDto
import com.syncrown.arpang.network.model.ResponseUserTokenDelDto
import com.syncrown.arpang.network.model.ResponseUserTokenRegDto
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArPangRepository {
    companion object {
        private const val BASE_URL_DEV = "http://192.168.0.13:8090"
        private const val BASE_URL_REAL = ""
    }

    private var arPangInterface: ArPangInterface

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val interceptorAuth = Interceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("authorization", AppDataPref.AccessToken).build()
            chain.proceed(request)
        }

        val client: okhttp3.OkHttpClient = okhttp3.OkHttpClient.Builder()
            .addInterceptor(interceptorAuth)
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()

        val gson: Gson = GsonBuilder().setLenient().create()

        arPangInterface = Retrofit.Builder()
            .baseUrl(BASE_URL_DEV)
            .client(client)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ArPangInterface::class.java)
    }

    /***********************************************************************************************
     * 각 뷰모델에서 사용하기 위한 라이브데이터
     **********************************************************************************************/
    //TODO 001. 사용자 토큰 등록
    val userTokenRegisterLiveDataRepository: MutableLiveData<NetworkResult<ResponseUserTokenRegDto>> =
        MutableLiveData()

    //TODO 002. 사용자 토큰 삭제
    val userTokenDeleteLiveDataRepository: MutableLiveData<NetworkResult<ResponseUserTokenDelDto>> =
        MutableLiveData()

    //TODO 003. 회원여부 확인
    val checkMemberLiveDataRepository: MutableLiveData<NetworkResult<ResponseCheckMember>> =
        MutableLiveData()

    //TODO 004. 가입신청
    val joinLiveDataRepository: MutableLiveData<NetworkResult<ResponseJoinDto>> = MutableLiveData()

    //TODO 005. 로그인
    val loginLiveDataRepository: MutableLiveData<NetworkResult<ResponseLoginDto>> =
        MutableLiveData()

    //TODO 006. 프로필 수정
    val updateProfileLiveDataRepository: MutableLiveData<NetworkResult<ResponseUpdateProfileDto>> =
        MutableLiveData()

    //TODO 007. 별명 중복체크
    val checkNickLiveDataRepository: MutableLiveData<NetworkResult<ResponseCheckNickNameDto>> =
        MutableLiveData()

    /***********************************************************************************************
     * API response를 라이브데이터에 추가
     **********************************************************************************************/
    //TODO 001. 사용자 토큰 등록
    fun requestUserTokenRegister(requestUserTokenRegDto: RequestUserTokenRegDto) {
        arPangInterface.postInsertUserToken(
            requestUserTokenRegDto.user_token.toString(),
            requestUserTokenRegDto.app_id.toString(),
            requestUserTokenRegDto.device_id.toString(),
            requestUserTokenRegDto.user_id
        ).enqueue(object : Callback<ResponseUserTokenRegDto> {
            override fun onResponse(
                call: Call<ResponseUserTokenRegDto>,
                response: Response<ResponseUserTokenRegDto>
            ) {
                if (response.body()?.msgCode.equals("SUCCESS")) {
                    userTokenRegisterLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    userTokenRegisterLiveDataRepository.postValue(
                        NetworkResult.Error(
                            response.body()?.msgCode
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ResponseUserTokenRegDto>, t: Throwable) {
                userTokenRegisterLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 002. 사용자 토큰 삭제
    fun requestUserTokenDelete(requestUserTokenDelDto: RequestUserTokenDelDto) {
        arPangInterface.postDeleteUserToken(
            requestUserTokenDelDto.app_id.toString(),
            requestUserTokenDelDto.device_id.toString()
        ).enqueue(object : Callback<ResponseUserTokenDelDto> {
            override fun onResponse(
                call: Call<ResponseUserTokenDelDto>,
                response: Response<ResponseUserTokenDelDto>
            ) {
                if (response.body()?.msgCode.equals("SUCCESS")) {
                    userTokenDeleteLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    userTokenDeleteLiveDataRepository.postValue(NetworkResult.Error(response.body()?.msgCode))
                }
            }

            override fun onFailure(call: Call<ResponseUserTokenDelDto>, t: Throwable) {
                userTokenDeleteLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 003. 회원여부 확인
    fun requestCheckMember(requestCheckMember: RequestCheckMember) {
        arPangInterface.postCheckMember(
            requestCheckMember.user_id
        ).enqueue(object : Callback<ResponseCheckMember> {
            override fun onResponse(
                call: Call<ResponseCheckMember>,
                response: Response<ResponseCheckMember>
            ) {
                if (response.code() == 200) {
                    checkMemberLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                }
            }

            override fun onFailure(call: Call<ResponseCheckMember>, t: Throwable) {
                checkMemberLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 004. 가입신청
    fun requestJoin(requestJoinDto: RequestJoinDto) {
        arPangInterface.postJoin(
            requestJoinDto.user_id, requestJoinDto.login_connt_accnt,
            requestJoinDto.userid_se, requestJoinDto.user_nm,
            requestJoinDto.use_se, requestJoinDto.mobl_no,
            requestJoinDto.email, requestJoinDto.nick_nm,
            requestJoinDto.stt_msg, requestJoinDto.nation,
            requestJoinDto.lang, requestJoinDto.birth_day,
            requestJoinDto.markt_recptn_agre, requestJoinDto.profile
        ).enqueue(object : Callback<ResponseJoinDto> {
            override fun onResponse(
                call: Call<ResponseJoinDto>,
                response: Response<ResponseJoinDto>
            ) {
                if (response.code() == 200) {
                    joinLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                }
            }

            override fun onFailure(call: Call<ResponseJoinDto>, t: Throwable) {
                joinLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 005. 로그인
    fun requestLogin(requestLoginDto: RequestLoginDto) {
        arPangInterface.postLogin(
            requestLoginDto.user_id
        ).enqueue(object : Callback<ResponseLoginDto> {
            override fun onResponse(
                call: Call<ResponseLoginDto>,
                response: Response<ResponseLoginDto>
            ) {
                if (response.code() == 200) {
                    val authorization = response.headers()["authorization"]
                    val reAuthorization = response.headers()["re-authorization"]

                    if (authorization != null) {
                        AppDataPref.AccessToken = authorization
                    }

                    if (reAuthorization != null) {
                        AppDataPref.reAuthorization = reAuthorization
                    }

                    loginLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                }
            }

            override fun onFailure(call: Call<ResponseLoginDto>, t: Throwable) {
                loginLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 006. 프로필 수정
    fun requestUpdateProfile(requestUpdateProfileDto: RequestUpdateProfileDto) {
        arPangInterface.postUpdateProfile(
            requestUpdateProfileDto.user_id,
            requestUpdateProfileDto.nick_nm,
            requestUpdateProfileDto.mobl_no,
            requestUpdateProfileDto.stt_msg,
            requestUpdateProfileDto.lang,
            requestUpdateProfileDto.profile
        ).enqueue(object : Callback<ResponseUpdateProfileDto> {
            override fun onResponse(
                call: Call<ResponseUpdateProfileDto>,
                response: Response<ResponseUpdateProfileDto>
            ) {
                if (response.code() == 200) {
                    updateProfileLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                }
            }

            override fun onFailure(call: Call<ResponseUpdateProfileDto>, t: Throwable) {
                updateProfileLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 007. 별명 중복 체크
    fun requestCheckNickName(requestCheckNickNameDto: RequestCheckNickNameDto) {
        arPangInterface.postCheckNickName(
            requestCheckNickNameDto.nick_nm
        ).enqueue(object : Callback<ResponseCheckNickNameDto> {
            override fun onResponse(
                call: Call<ResponseCheckNickNameDto>,
                response: Response<ResponseCheckNickNameDto>
            ) {
                if (response.code() == 200) {
                    checkNickLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                }
            }

            override fun onFailure(call: Call<ResponseCheckNickNameDto>, t: Throwable) {
                checkNickLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }
}