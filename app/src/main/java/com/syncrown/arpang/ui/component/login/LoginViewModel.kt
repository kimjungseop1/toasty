package com.syncrown.arpang.ui.component.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.syncrown.arpang.R
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NaverClient
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()
    private val checkMemberResponseLiveData: LiveData<NetworkResult<ResponseCheckMember>> =
        arPangRepository.checkMemberLiveDataRepository
    private val joinMemberResponseLiveData: LiveData<NetworkResult<ResponseJoinDto>> =
        arPangRepository.joinLiveDataRepository

    /**
     * =============================================================================================
     * 회원여부 체크
     * =============================================================================================
     */
    private fun checkMember(memberId: String) {
        viewModelScope.launch {
            val requestCheckMember = RequestCheckMember()
            requestCheckMember.apply {
                user_id = memberId
            }

            arPangRepository.requestCheckMember(requestCheckMember)
        }
    }

    fun checkMemberResponseLiveData(): LiveData<NetworkResult<ResponseCheckMember>> {
        return checkMemberResponseLiveData
    }

    /**
     * =============================================================================================
     * 가입신청
     * =============================================================================================
     */
    fun joinMember(requestJoinDto: RequestJoinDto) {
        viewModelScope.launch {
            arPangRepository.requestJoin(requestJoinDto)
        }
    }

    fun joinMemberResponseLiveData(): LiveData<NetworkResult<ResponseJoinDto>> {
        return joinMemberResponseLiveData
    }

    /**
     * =============================================================================================
     * 구글
     * =============================================================================================
     */
    private val _signInResult = MutableLiveData<Result<GoogleSignInAccount>>()
    val signInResult: LiveData<Result<GoogleSignInAccount>> = _signInResult

    private lateinit var signInClient: SignInClient

    fun initialize(context: Context) {
        signInClient = Identity.getSignInClient(context)
    }

    fun initiateSignIn(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>) {
        viewModelScope.launch {
            try {
                val signInRequest = GetSignInIntentRequest.builder()
                    .setServerClientId(context.getString(R.string.google_client_id))
                    .build()

                val pendingIntent = signInClient.getSignInIntent(signInRequest).await()
                launcher.launch(IntentSenderRequest.Builder(pendingIntent).build())
            } catch (e: Exception) {
                _signInResult.value = Result.failure(e)
            }
        }
    }

    fun handleSignInResult(data: Intent?) {
        viewModelScope.launch {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.await()
                _signInResult.value = Result.success(account)
            } catch (e: ApiException) {
                _signInResult.value = Result.failure(e)
            }
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            try {
                val signOutRequest = Identity.getSignInClient(context).signOut().await()
                // Sign-out succeeded, handle accordingly
            } catch (e: Exception) {
                // Handle exception during sign-out
            }
        }

    }

    /**
     * =============================================================================================
     * 페이스북
     * =============================================================================================
     */
    fun facebookLogin(activity: Activity, callbackManager: CallbackManager) {
        LoginManager.getInstance().logInWithReadPermissions(
            activity, listOf("email", "public_profile")
        )

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.e("Callback :: ", "onSuccess")
                    requestMe(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.e("Callback :: ", "onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.e("Callback :: ", "onError : ${error.message}")
                }
            })

    }

    private fun requestMe(token: AccessToken) {
        val graphRequest = GraphRequest.newMeRequest(token) { `object`, _ ->
            Log.e("result", `object`.toString())
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,gender,birthday")
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }

    /**
     * =============================================================================================
     * 카카오
     * =============================================================================================
     */
    fun kakaoLogin(context: Context) {
        viewModelScope.launch {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token: OAuthToken?, error: Throwable? ->
                    if (token != null) {
                        val snsToken = token.accessToken
                        Log.e(TAG, snsToken)
                        getKaKaoProfile(context, snsToken)
                    }

                    error?.printStackTrace()
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context) { token: OAuthToken?, error: Throwable? ->
                    if (token != null) {
                        val snsToken = token.accessToken
                        getKaKaoProfile(context, snsToken)
                    }

                    error?.printStackTrace()
                }
            }
        }
    }

    private fun getKaKaoProfile(context: Context, snsToken: String) {
        UserApiClient.instance.me { user: User?, throwable: Throwable? ->
            if (user != null) {
                //TODO
                val email = user.kakaoAccount!!.email

                checkMember("k $email")
            } else {
                Toast.makeText(context, "계정 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            }
            throwable?.printStackTrace()
        }
    }


    /**
     * =============================================================================================
     * 네이버
     * =============================================================================================
     */
    fun naverLogin(context: Context) {
        NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
            override fun onSuccess() {
                // 로그인 성공 처리
                val accessToken = NaverIdLoginSDK.getAccessToken()
                if (accessToken != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        fetchNaverUserProfile(accessToken)
                    }
                }
            }

            override fun onFailure(httpStatus: Int, message: String) {
                // 로그인 실패 처리
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDesc = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("NaverLogin", "Error: $errorCode, $errorDesc")
            }

            override fun onError(errorCode: Int, message: String) {
                // 로그인 에러 처리
            }
        })
    }

    private suspend fun fetchNaverUserProfile(accessToken: String?) {
        try {
            val naverApiService = NaverClient.instance
            val response = naverApiService.getUserProfile("Bearer $accessToken")

            withContext(Dispatchers.Main) {
                // 가져온 이메일 및 사용자 정보 처리
                val email = response.response.email
                val name = response.response.name

                Log.d("jung", "User email: $email")
                Log.d("jung", "User name: $name")

                // 회원여부체크
                checkMember("n $email")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.printStackTrace()
            }
        }
    }

    /**
     * =============================================================================================
     * 애플 - AppleSignInDialog.kt 로 변경
     * =============================================================================================
     */
}