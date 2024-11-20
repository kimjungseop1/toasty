package com.syncrown.arpang.ui.component.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.R
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NaverClient
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONException
import kotlin.coroutines.resumeWithException


class LoginViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /**
     * =============================================================================================
     * 회원여부 체크
     * =============================================================================================
     */
    private val checkMemberResponseLiveData: LiveData<NetworkResult<ResponseCheckMember>> =
        arPangRepository.checkMemberLiveDataRepository

    fun checkMember(memberId: String) {
        viewModelScope.launch {
            val requestCheckMember = RequestCheckMember()
            requestCheckMember.apply {
                user_id = memberId

                AppDataPref.userId = user_id
            }

            arPangRepository.requestCheckMember(requestCheckMember)
        }
    }

    fun checkMemberResponseLiveData(): LiveData<NetworkResult<ResponseCheckMember>> {
        return checkMemberResponseLiveData
    }

    /**
     * =============================================================================================
     * 로그인
     * =============================================================================================
     */
    private val loginResponseLiveData: LiveData<NetworkResult<ResponseLoginDto>> =
        arPangRepository.loginLiveDataRepository

    fun login(requestLoginDto: RequestLoginDto) {
        viewModelScope.launch {
            arPangRepository.requestLogin(requestLoginDto)
        }
    }

    fun loginResponseLiveData(): LiveData<NetworkResult<ResponseLoginDto>> {
        return loginResponseLiveData
    }

    /**
     * =============================================================================================
     * 구글
     * =============================================================================================
     */
    private lateinit var _googleSignInClient: GoogleSignInClient

    private val _googleAccount = MutableLiveData<GoogleSignInAccount?>()
    val googleAccount: LiveData<GoogleSignInAccount?> = _googleAccount

    fun initialize(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_web_client_id))
            .requestEmail()
            .build()

        _googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getGoogleSignInClient(): GoogleSignInClient {
        return _googleSignInClient
    }

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            _googleAccount.value = account
        } catch (e: ApiException) {
            _googleAccount.value = null
        }
    }

    fun signOut() {
        _googleSignInClient.signOut().addOnCompleteListener {
            _googleAccount.value = null
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
                    Log.e("jung", "onSuccess")
                    requestMe(activity, loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.e("jung", "onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.e("jung", "onError : ${error.message}")
                }
            })

    }

    private fun requestMe(activity: Activity, token: AccessToken) {
        val graphRequest = GraphRequest.newMeRequest(token) { `object`, _ ->
            try {
                val uniqueId = `object`?.optString("id", null) ?: ""
                val email = `object`?.optString("email", null) ?: ""

                // Log the results
                Log.e("jung", uniqueId)

                val userId = "f-$uniqueId"

                AppDataPref.login_connect_site = "f"
                AppDataPref.userEmail = email

                checkMember(userId)
            } catch (e: JSONException) {
                e.printStackTrace()
                Log.e("jung", "Failed to parse user data: ${e.message}")
            }
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
    fun kakaoLogin(activity: Activity) {
        viewModelScope.launch {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
                UserApiClient.instance.loginWithKakaoTalk(activity) { token: OAuthToken?, error: Throwable? ->
                    if (token != null) {
                        val snsToken = token.accessToken
                        Log.e("jung", snsToken)
                        getKaKaoProfile(activity, snsToken)
                    }

                    error?.printStackTrace()
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(activity) { token: OAuthToken?, error: Throwable? ->
                    if (token != null) {
                        val snsToken = token.accessToken
                        getKaKaoProfile(activity, snsToken)
                    }

                    error?.printStackTrace()
                }
            }
        }
    }

    private fun getKaKaoProfile(activity: Activity, snsToken: String) {
        UserApiClient.instance.me { user: User?, throwable: Throwable? ->
            if (user != null) {
                //TODO
                val email = user.kakaoAccount?.email
                val id = user.id
                var userId = "k-$id"

                AppDataPref.login_connect_site = "k"
                AppDataPref.userEmail = email.toString()

                checkMember(userId)
            } else {
                Toast.makeText(activity, "계정 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            }
            throwable?.printStackTrace()
        }
    }


    /**
     * =============================================================================================
     * 네이버
     * =============================================================================================
     */
    fun naverLogin(activity: Activity) {
        NaverIdLoginSDK.authenticate(activity, object : OAuthLoginCallback {
            override fun onSuccess() {
                // 로그인 성공 처리
                val accessToken = NaverIdLoginSDK.getAccessToken()
                if (accessToken != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        fetchNaverUserProfile(activity)

                        fetchNaverUserEmail(accessToken)
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

    private suspend fun fetchNaverUserProfile(activity: Activity) {
        try {
            val result = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine { continuation ->
                    NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                        override fun onSuccess(result: NidProfileResponse) {
                            continuation.resume(result) {}
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            continuation.resumeWithException(Exception("Profile API failed with status $httpStatus: $message"))
                        }

                        override fun onError(errorCode: Int, message: String) {
                            continuation.resumeWithException(Exception("Profile API error: $message"))
                        }
                    })
                }
            }

            val uniqueId = result.profile?.id
            Log.e("jung", "uniqueid : $uniqueId")
            val userId = "n-$uniqueId"
            AppDataPref.login_connect_site = "n"

            withContext(Dispatchers.Main) {
                checkMember(userId)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun fetchNaverUserEmail(accessToken: String?) {
        try {
            val naverApiService = NaverClient.instance
            val response = naverApiService.getUserProfile("Bearer $accessToken")

            withContext(Dispatchers.Main) {
                // 가져온 이메일 및 사용자 정보 처리
                val email = response.response.email
                val name = response.response.name

                Log.d("jung", "User email: $email")
                Log.d("jung", "User name: $name")

                AppDataPref.userEmail = email
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.printStackTrace()
            }
        }
    }


    /**
     * =============================================================================================
     * 애플
     * =============================================================================================
     */
    fun appleLogin(activity: Activity, ) {
        val provider = OAuthProvider.newBuilder("apple.com")

        provider.setScopes(listOf("email", "name"))
        provider.addCustomParameter("locale", "ko_KR")

        val auth = Firebase.auth
        val pending = auth.pendingAuthResult

        pending?.addOnSuccessListener { authResult ->
            handleAppleLoginSuccess(authResult)
        }?.addOnFailureListener { e ->
            Log.e("AppleLogin", "Error: ${e.localizedMessage}")
        }

        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                handleAppleLoginSuccess(authResult)
            }
            .addOnFailureListener { e ->
                Log.e("AppleLogin", "Error: ${e.localizedMessage}")
            }
    }

    private fun handleAppleLoginSuccess(authResult: AuthResult) {
        val uniqueId = authResult.user?.uid

        val oauthCredential: OAuthCredential = authResult.credential as OAuthCredential
        val token = oauthCredential.accessToken

        Log.e("jung", "apple uniqueid : $uniqueId")
        Log.e("jung", "apple credential : $token")
        val userId = "a-$uniqueId"
        AppDataPref.login_connect_site = "a"
        AppDataPref.userEmail = authResult.user?.email.toString()

        checkMember(userId)
    }
}