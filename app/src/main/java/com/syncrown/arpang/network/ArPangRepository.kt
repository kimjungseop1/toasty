package com.syncrown.arpang.network

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.network.model.RequestAddCommentDto
import com.syncrown.arpang.network.model.RequestAllFavoriteDto
import com.syncrown.arpang.network.model.RequestAppMainDto
import com.syncrown.arpang.network.model.RequestAppMenuByCartridgeDto
import com.syncrown.arpang.network.model.RequestBlockUserListDto
import com.syncrown.arpang.network.model.RequestCartridgeByAppMenuDto
import com.syncrown.arpang.network.model.RequestCartridgeListByTagDto
import com.syncrown.arpang.network.model.RequestCartridgeListDto
import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestCheckNickNameDto
import com.syncrown.arpang.network.model.RequestCommentListDto
import com.syncrown.arpang.network.model.RequestCommentReportDto
import com.syncrown.arpang.network.model.RequestCommonListDto
import com.syncrown.arpang.network.model.RequestContentReportDto
import com.syncrown.arpang.network.model.RequestDelCommentDto
import com.syncrown.arpang.network.model.RequestDeleteStorageDto
import com.syncrown.arpang.network.model.RequestDetailContentHashTagDto
import com.syncrown.arpang.network.model.RequestEditContentHashTagDto
import com.syncrown.arpang.network.model.RequestFavoriteDto
import com.syncrown.arpang.network.model.RequestIgnoreTagCheckDto
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.RequestMainBannerDto
import com.syncrown.arpang.network.model.RequestMultiCommonListDto
import com.syncrown.arpang.network.model.RequestMyFavoriteDto
import com.syncrown.arpang.network.model.RequestNoticeDetailDto
import com.syncrown.arpang.network.model.RequestNoticeListDto
import com.syncrown.arpang.network.model.RequestPublicContentSettingDto
import com.syncrown.arpang.network.model.RequestRecommendTagListDto
import com.syncrown.arpang.network.model.RequestResultMatchDto
import com.syncrown.arpang.network.model.RequestSaveEditorDto
import com.syncrown.arpang.network.model.RequestScrapContentDto
import com.syncrown.arpang.network.model.RequestScrapUpdateDto
import com.syncrown.arpang.network.model.RequestSearchingMatchDto
import com.syncrown.arpang.network.model.RequestShareContentAllOpenListDto
import com.syncrown.arpang.network.model.RequestShareContentUserOpenListDto
import com.syncrown.arpang.network.model.RequestShareDetailDto
import com.syncrown.arpang.network.model.RequestStorageContentListDto
import com.syncrown.arpang.network.model.RequestStorageDetailDto
import com.syncrown.arpang.network.model.RequestSubscribeByMeDto
import com.syncrown.arpang.network.model.RequestSubscribeByMyDto
import com.syncrown.arpang.network.model.RequestSubscribeTotalDto
import com.syncrown.arpang.network.model.RequestSubscribeUpdateDto
import com.syncrown.arpang.network.model.RequestSubscribeUserContentListDto
import com.syncrown.arpang.network.model.RequestTagsByCartridgeDto
import com.syncrown.arpang.network.model.RequestTargetUserBlockDto
import com.syncrown.arpang.network.model.RequestTargetUserReportDto
import com.syncrown.arpang.network.model.RequestTemplateListDto
import com.syncrown.arpang.network.model.RequestUpdateProfileDto
import com.syncrown.arpang.network.model.RequestUserAlertListDto
import com.syncrown.arpang.network.model.RequestUserAlertSettingDto
import com.syncrown.arpang.network.model.RequestUserProfileDto
import com.syncrown.arpang.network.model.RequestUserTokenDelDto
import com.syncrown.arpang.network.model.RequestUserTokenRegDto
import com.syncrown.arpang.network.model.RequestWithdrawalDto
import com.syncrown.arpang.network.model.ResponseAddCommentDto
import com.syncrown.arpang.network.model.ResponseAllFavoriteDto
import com.syncrown.arpang.network.model.ResponseAppMainDto
import com.syncrown.arpang.network.model.ResponseAppMenuByCartridgeDto
import com.syncrown.arpang.network.model.ResponseBlockUserListDto
import com.syncrown.arpang.network.model.ResponseCartridgeByAppMenuDto
import com.syncrown.arpang.network.model.ResponseCartridgeListByTagDto
import com.syncrown.arpang.network.model.ResponseCartridgeListDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseCheckNickNameDto
import com.syncrown.arpang.network.model.ResponseCommentListDto
import com.syncrown.arpang.network.model.ResponseCommentReportDto
import com.syncrown.arpang.network.model.ResponseCommonListDto
import com.syncrown.arpang.network.model.ResponseContentReportDto
import com.syncrown.arpang.network.model.ResponseDelCommentDto
import com.syncrown.arpang.network.model.ResponseDeleteStorageDto
import com.syncrown.arpang.network.model.ResponseDetailContentHashTagDto
import com.syncrown.arpang.network.model.ResponseEditContentHashTagDto
import com.syncrown.arpang.network.model.ResponseFavoriteDto
import com.syncrown.arpang.network.model.ResponseIgnoreTagCheckDto
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.network.model.ResponseMainBannerDto
import com.syncrown.arpang.network.model.ResponseMultiCommonListDto
import com.syncrown.arpang.network.model.ResponseMyFavoriteDto
import com.syncrown.arpang.network.model.ResponseNoticeDetailDto
import com.syncrown.arpang.network.model.ResponseNoticeListDto
import com.syncrown.arpang.network.model.ResponsePopularTagDto
import com.syncrown.arpang.network.model.ResponsePublicContentSettingDto
import com.syncrown.arpang.network.model.ResponseRecommendTagListDto
import com.syncrown.arpang.network.model.ResponseResultMatchDto
import com.syncrown.arpang.network.model.ResponseSaveEditorDto
import com.syncrown.arpang.network.model.ResponseScrapContentDto
import com.syncrown.arpang.network.model.ResponseScrapUpdateDto
import com.syncrown.arpang.network.model.ResponseSearchingMatchDto
import com.syncrown.arpang.network.model.ResponseShareContentAllOpenListDto
import com.syncrown.arpang.network.model.ResponseShareContentUserOpenListDto
import com.syncrown.arpang.network.model.ResponseShareDetailDto
import com.syncrown.arpang.network.model.ResponseStorageContentListDto
import com.syncrown.arpang.network.model.ResponseStorageDetailDto
import com.syncrown.arpang.network.model.ResponseSubscribeListDto
import com.syncrown.arpang.network.model.ResponseSubscribeTotalDto
import com.syncrown.arpang.network.model.ResponseSubscribeUpdateDto
import com.syncrown.arpang.network.model.ResponseSubscribeUserContentListDto
import com.syncrown.arpang.network.model.ResponseTagsByCartridgeDto
import com.syncrown.arpang.network.model.ResponseTargetUserBlockDto
import com.syncrown.arpang.network.model.ResponseTargetUserReportDto
import com.syncrown.arpang.network.model.ResponseTemplateListDto
import com.syncrown.arpang.network.model.ResponseUpdateProfileDto
import com.syncrown.arpang.network.model.ResponseUserAlertListDto
import com.syncrown.arpang.network.model.ResponseUserAlertSettingDto
import com.syncrown.arpang.network.model.ResponseUserProfileDto
import com.syncrown.arpang.network.model.ResponseUserTokenDelDto
import com.syncrown.arpang.network.model.ResponseUserTokenRegDto
import com.syncrown.arpang.network.model.ResponseWithdrawalDto
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArPangRepository {
    companion object {
        const val BASE_URL_DEV = "http://192.168.0.132:8090"

        //        const val BASE_URL_DEV = "http://192.168.0.13:8090"
        const val BASE_URL_REAL = ""
    }

    private var arPangInterface: ArPangInterface

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val interceptorAuth = Interceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("authorization", AppDataPref.AccessToken)
                .addHeader("re-authorization", AppDataPref.reAuthorization)
                .build()
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

    //TODO 006-1 프로필 정보 조회
    val userprofileLiveDataRepository: MutableLiveData<NetworkResult<ResponseUserProfileDto>> =
        MutableLiveData()

    //TODO 007. 별명 중복체크
    val checkNickLiveDataRepository: MutableLiveData<NetworkResult<ResponseCheckNickNameDto>> =
        MutableLiveData()

    //TODO 008. 회원 탈퇴
    val withdrawalLiveDataRepository: MutableLiveData<NetworkResult<ResponseWithdrawalDto>> =
        MutableLiveData()

    //TODO 009. 공통코드 리스트
    val commonListLiveDataRepository: MutableLiveData<NetworkResult<ResponseCommonListDto>> =
        MutableLiveData()

    //TODO 009-1. 공통코드 리스트(코드값 복수개)
    val multiCommonListLiveDataRepository: MutableLiveData<NetworkResult<ResponseMultiCommonListDto>> =
        MutableLiveData()

    //TODO 010. 앱메인메뉴 리스트
    val appMainMenuLiveDataRepository: MutableLiveData<NetworkResult<ResponseAppMainDto>> =
        MutableLiveData()

    //TODO 011. 카트리지 리스트
    val cartridgeListLiveDataRepository: MutableLiveData<NetworkResult<ResponseCartridgeListDto>> =
        MutableLiveData()

    //TODO 011-1 추천태그 리스트
    val recommendTagListLiveDataRepository: MutableLiveData<NetworkResult<ResponseRecommendTagListDto>> =
        MutableLiveData()

    //TODO 012. 카트리지별 앱메뉴 리스트
    val appMenuByCartridgeLiveDataRepository: MutableLiveData<NetworkResult<ResponseAppMenuByCartridgeDto>> =
        MutableLiveData()

    //TODO 013. 앱메뉴별 카트리지 리스트
    val cartridgeByAppMenuLiveDataRepository: MutableLiveData<NetworkResult<ResponseCartridgeByAppMenuDto>> =
        MutableLiveData()

    //TODO 014. 카트리지별 추천태그 리스트
    val tagsByCartridgeLiveDataRepository: MutableLiveData<NetworkResult<ResponseTagsByCartridgeDto>> =
        MutableLiveData()

    //TODO 015. 추천태그별 카트리지 리스트를 가져온다
    val cartridgeListByTagsLiveDataRepository: MutableLiveData<NetworkResult<ResponseCartridgeListByTagDto>> =
        MutableLiveData()

    //TODO 016. 사용자 알림 설정
    val userAlertSettingLiveDataRepository: MutableLiveData<NetworkResult<ResponseUserAlertSettingDto>> =
        MutableLiveData()

    //TODO 017. 사용자 알림 리스트
    val userAlertListLiveDataRepository: MutableLiveData<NetworkResult<ResponseUserAlertListDto>> =
        MutableLiveData()

    //TODO 018. 템플릿 리스트
    val templateListLiveDataRepository: MutableLiveData<NetworkResult<ResponseTemplateListDto>> =
        MutableLiveData()

    // 019. 금지해시태그조회
    val ignoreTagCheckLiveDataRepository: MutableLiveData<NetworkResult<ResponseIgnoreTagCheckDto>> =
        MutableLiveData()

    // 020. 에디터 Object 저장(일괄)
    val saveEditorLiveDataRepository: MutableLiveData<NetworkResult<ResponseSaveEditorDto>> =
        MutableLiveData()

    //TODO 021. 보관함(사용자) 저장 컨텐츠 리스트
    val storageContentListLiveDataRepository: MutableLiveData<NetworkResult<ResponseStorageContentListDto>> =
        MutableLiveData()

    //TODO 022. 보관함(사용자) 저장 컨텐츠 상세조회
    val storageContentDetailDataRepository: MutableLiveData<NetworkResult<ResponseStorageDetailDto>> =
        MutableLiveData()

    //TODO 023. 사용자가 공유(공개)한 컨텐츠 리스트
    var shareContentUserOpenListLiveDataRepository: MutableLiveData<NetworkResult<ResponseShareContentUserOpenListDto>> =
        MutableLiveData()

    //TODO 024. 전체 공유(공개) 컨텐츠 리스트
    var shareContentAllOpenListLiveDataRepository: MutableLiveData<NetworkResult<ResponseShareContentAllOpenListDto>> =
        MutableLiveData()

    //TODO 025. 공개(공유)된 컨텐츠 상세조회
    var shareDetailLiveDataRepository: MutableLiveData<NetworkResult<ResponseShareDetailDto>> =
        MutableLiveData()

    //TODO 026. 전체 인기있는 해시태그 리스트(상위5건)
    var allFavoriteHashTagLiveDataRepository: MutableLiveData<NetworkResult<ResponseAllFavoriteDto>> =
        MutableLiveData()

    //TODO 027. 내가등록한 해시태그 리스트 가져오기 (상위5건)
    var myFavoriteHashTagLiveDataRepository: MutableLiveData<NetworkResult<ResponseMyFavoriteDto>> =
        MutableLiveData()

    //TODO 028. 댓글 리스트
    val commentListLiveDataRepository: MutableLiveData<NetworkResult<ResponseCommentListDto>> =
        MutableLiveData()

    //TODO 029. 댓글 작성
    val addCommentLiveDataRepository: MutableLiveData<NetworkResult<ResponseAddCommentDto>> =
        MutableLiveData()

    //TODO 030. 댓글 삭제
    val delCommentLiveDataRepository: MutableLiveData<NetworkResult<ResponseDelCommentDto>> =
        MutableLiveData()

    //TODO 031. 상세컨텐츠의 해시태그 리스트
    val detailContentHashListRepository: MutableLiveData<NetworkResult<ResponseDetailContentHashTagDto>> =
        MutableLiveData()

    //TODO 032. 컨텐츠 공개여부 설정하기
    val publicContentSettingLiveDataRepository: MutableLiveData<NetworkResult<ResponsePublicContentSettingDto>> =
        MutableLiveData()

    //TODO 033-1. 구독하기 등록/해제
    val subscribeUpdateLiveDataRepository: MutableLiveData<NetworkResult<ResponseSubscribeUpdateDto>> =
        MutableLiveData()

    //TODO 033-2. 내가 구독한 사용자 목록
    val subscribeListByMyLiveDataRepository: MutableLiveData<NetworkResult<ResponseSubscribeListDto>> =
        MutableLiveData()

    //TODO 033-3. 나를 구독한 사용자 목록
    val subscribeListByMeLiveDataRepository: MutableLiveData<NetworkResult<ResponseSubscribeListDto>> =
        MutableLiveData()

    //TODO 033-5. 구독자 전체 갯수 조회
    val subscribeTotalCntLiveDataRepository: MutableLiveData<NetworkResult<ResponseSubscribeTotalDto>> =
        MutableLiveData()

    //TODO 034. 상세컨텐츠의 해시태그 리스트 편집
    val editContentHashTagLiveDataRepository: MutableLiveData<NetworkResult<ResponseEditContentHashTagDto>> =
        MutableLiveData()

    //TODO 035. 보관함 컨텐츠 삭제
    val storageContentDeleteLiveDataRepository: MutableLiveData<NetworkResult<ResponseDeleteStorageDto>> =
        MutableLiveData()

    //TODO 036. 댓글신고(다른사람이 쓴 댓글)
    val commentReportLiveDataRepository: MutableLiveData<NetworkResult<ResponseCommentReportDto>> =
        MutableLiveData()

    //TODO 037. 구독한 사용자의 공유(공개)한 컨텐츠 리스트
    val subscribeUserContentListLiveDataRepository: MutableLiveData<NetworkResult<ResponseSubscribeUserContentListDto>> =
        MutableLiveData()

    //TODO 038. 좋아요 등록/삭제
    val favoriteUpdateLiveDataRepository: MutableLiveData<NetworkResult<ResponseFavoriteDto>> =
        MutableLiveData()

    //TODO 039. 공유공간 상세 게시글을 신고
    val contentReportLiveDataRepository: MutableLiveData<NetworkResult<ResponseContentReportDto>> =
        MutableLiveData()

    //TODO 040. 스크랩 등록/삭제
    val scrapUpdateLiveDataRepository: MutableLiveData<NetworkResult<ResponseScrapUpdateDto>> =
        MutableLiveData()

    //TODO 041. 스크랩 컨텐츠 리스트
    val scrapContentListLiveDataRepository: MutableLiveData<NetworkResult<ResponseScrapContentDto>> =
        MutableLiveData()

    //TODO 042. 특정 사용자 신고하기
    val targetUserReportLiveDataRepository: MutableLiveData<NetworkResult<ResponseTargetUserReportDto>> =
        MutableLiveData()

    //TODO 043. 특정 사용자 차단 등록/해제
    val targetUserBlockUpdateLiveDataRepository: MutableLiveData<NetworkResult<ResponseTargetUserBlockDto>> =
        MutableLiveData()

    //TODO 044. 차단한 사용자 목록
    val blockUserListLiveDataRepository: MutableLiveData<NetworkResult<ResponseBlockUserListDto>> =
        MutableLiveData()

    //TODO 045. 공지사항 리스트
    val noticeListLiveDataRepository: MutableLiveData<NetworkResult<ResponseNoticeListDto>> =
        MutableLiveData()

    //TODO 046. 공지사항 상세보기
    val noticeDetailLiveDataRepository: MutableLiveData<NetworkResult<ResponseNoticeDetailDto>> =
        MutableLiveData()

    //TODO 047. 인기 검색 태그
    val popularTagSearchLiveDataRepository: MutableLiveData<NetworkResult<ResponsePopularTagDto>> =
        MutableLiveData()

    //TODO 048. 입력된 단어가 포함된 이름/공유/보관함 검색중 리스트
    val searchingMatchListLiveDataRepository: MutableLiveData<NetworkResult<ResponseSearchingMatchDto>> =
        MutableLiveData()

    //TODO 049. 입력된 단어가 포함된 이름/공유/보관함 검색결과 리스트
    val resultMatchingLiveDataRepository: MutableLiveData<NetworkResult<ResponseResultMatchDto>> =
        MutableLiveData()

    //TODO 050. 메인베너, 팝업베너, 이렇게활용 리스트
    val mainBannerLiveDataRepository: MutableLiveData<NetworkResult<ResponseMainBannerDto>> =
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
            requestJoinDto.markt_recptn_agre, requestJoinDto.profile,
            requestJoinDto.app_id
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
                } else {
                    updateProfileLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseUpdateProfileDto>, t: Throwable) {
                updateProfileLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 006-1. 사용자 정보 조회
    fun requestUserProfile(requestUserProfileDto: RequestUserProfileDto) {
        arPangInterface.postUserProfile(
            requestUserProfileDto.user_id
        ).enqueue(object : Callback<ResponseUserProfileDto> {
            override fun onResponse(
                call: Call<ResponseUserProfileDto>,
                response: Response<ResponseUserProfileDto>
            ) {
                if (response.code() == 200) {
                    userprofileLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    userprofileLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseUserProfileDto>, t: Throwable) {
                userprofileLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 007. 별명 중복 체크
    fun requestCheckNickName(requestCheckNickNameDto: RequestCheckNickNameDto) {
        arPangInterface.postCheckNickName(
            requestCheckNickNameDto.user_id,
            requestCheckNickNameDto.nick_nm
        ).enqueue(object : Callback<ResponseCheckNickNameDto> {
            override fun onResponse(
                call: Call<ResponseCheckNickNameDto>,
                response: Response<ResponseCheckNickNameDto>
            ) {
                if (response.code() == 200) {
                    checkNickLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    checkNickLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseCheckNickNameDto>, t: Throwable) {
                checkNickLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 008. 회원 탈퇴
    fun requestWithdrawal(requestWithdrawalDto: RequestWithdrawalDto) {
        arPangInterface.postWithdrawal(
            requestWithdrawalDto.user_id,
            requestWithdrawalDto.break_reson,
            requestWithdrawalDto.etc_reson
        ).enqueue(object : Callback<ResponseWithdrawalDto> {
            override fun onResponse(
                call: Call<ResponseWithdrawalDto>,
                response: Response<ResponseWithdrawalDto>
            ) {
                if (response.code() == 200) {
                    withdrawalLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    withdrawalLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseWithdrawalDto>, t: Throwable) {
                withdrawalLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 009. 공통코드 리스트
    fun requestCommonList(requestCommonListDto: RequestCommonListDto) {
        arPangInterface.postCommonList(
            requestCommonListDto.lcode
        ).enqueue(object : Callback<ResponseCommonListDto> {
            override fun onResponse(
                call: Call<ResponseCommonListDto>,
                response: Response<ResponseCommonListDto>
            ) {
                if (response.code() == 200) {
                    commonListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    commonListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseCommonListDto>, t: Throwable) {
                commonListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 009-1. 공통코드 리스트(코드값 복수개)
    fun requestMultiCommonList(requestMultiCommonListDto: RequestMultiCommonListDto) {
        arPangInterface.postMultiCommonList(
            requestMultiCommonListDto.lcode,
            requestMultiCommonListDto.mcode
        ).enqueue(object : Callback<ResponseMultiCommonListDto> {
            override fun onResponse(
                call: Call<ResponseMultiCommonListDto>,
                response: Response<ResponseMultiCommonListDto>
            ) {
                if (response.code() == 200) {
                    multiCommonListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    multiCommonListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseMultiCommonListDto>, t: Throwable) {
                multiCommonListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 010. 앱메인메뉴 리스트
    fun requestAppMain(requestAppMainDto: RequestAppMainDto) {
        arPangInterface.postAppMain(
            requestAppMainDto.app_id.toString()
        ).enqueue(object : Callback<ResponseAppMainDto> {
            override fun onResponse(
                call: Call<ResponseAppMainDto>,
                response: Response<ResponseAppMainDto>
            ) {
                if (response.code() == 200) {
                    appMainMenuLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    appMainMenuLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseAppMainDto>, t: Throwable) {
                appMainMenuLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 011. 카트리지 리스트
    fun requestCartridgeList(requestCartridgeListDto: RequestCartridgeListDto) {
        arPangInterface.postCartridgeList(
            requestCartridgeListDto.app_id,
            requestCartridgeListDto.device_os,
            requestCartridgeListDto.ctge_no,
            requestCartridgeListDto.ctge_nm,
            requestCartridgeListDto.ctge_model,
            requestCartridgeListDto.ctge_model_abbr,
            requestCartridgeListDto.character_brand
        ).enqueue(object : Callback<ResponseCartridgeListDto> {
            override fun onResponse(
                call: Call<ResponseCartridgeListDto>,
                response: Response<ResponseCartridgeListDto>
            ) {
                if (response.code() == 200) {
                    cartridgeListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    cartridgeListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseCartridgeListDto>, t: Throwable) {
                cartridgeListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 011-1 추천태그 리스트
    fun requestRecommendTagList(requestRecommendTagListDto: RequestRecommendTagListDto) {
        arPangInterface.postRecommendAllList(
            requestRecommendTagListDto.app_id
        ).enqueue(object : Callback<ResponseRecommendTagListDto> {
            override fun onResponse(
                call: Call<ResponseRecommendTagListDto>,
                response: Response<ResponseRecommendTagListDto>
            ) {
                if (response.code() == 200) {
                    recommendTagListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    recommendTagListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseRecommendTagListDto>, t: Throwable) {
                recommendTagListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 012. 카트리지별 앱메뉴 리스트
    fun requestAppMenuByCartridge(requestAppMenuByCartridgeDto: RequestAppMenuByCartridgeDto) {
        arPangInterface.postAppMenuByCartridge(
            requestAppMenuByCartridgeDto.ctge_no,
            requestAppMenuByCartridgeDto.app_id
        ).enqueue(object : Callback<ResponseAppMenuByCartridgeDto> {
            override fun onResponse(
                call: Call<ResponseAppMenuByCartridgeDto>,
                response: Response<ResponseAppMenuByCartridgeDto>
            ) {
                if (response.code() == 200) {
                    appMenuByCartridgeLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    appMenuByCartridgeLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseAppMenuByCartridgeDto>, t: Throwable) {
                appMenuByCartridgeLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 013. 앱메뉴별 카트리지 리스트
    fun requestCartridgeByAppMenu(requestCartridgeByAppMenuDto: RequestCartridgeByAppMenuDto) {
        arPangInterface.postCartridgeByAppMenu(
            requestCartridgeByAppMenuDto.menu_code,
            requestCartridgeByAppMenuDto.app_id
        ).enqueue(object : Callback<ResponseCartridgeByAppMenuDto> {
            override fun onResponse(
                call: Call<ResponseCartridgeByAppMenuDto>,
                response: Response<ResponseCartridgeByAppMenuDto>
            ) {
                if (response.code() == 200) {
                    cartridgeByAppMenuLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    cartridgeByAppMenuLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseCartridgeByAppMenuDto>, t: Throwable) {
                cartridgeByAppMenuLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 014. 카트리지별 추천태그 리스트
    fun requestTagsByCartridge(requestTagsByCartridgeDto: RequestTagsByCartridgeDto) {
        arPangInterface.postTagsByCartridge(
            requestTagsByCartridgeDto.ctge_no,
            requestTagsByCartridgeDto.app_id
        ).enqueue(object : Callback<ResponseTagsByCartridgeDto> {
            override fun onResponse(
                call: Call<ResponseTagsByCartridgeDto>,
                response: Response<ResponseTagsByCartridgeDto>
            ) {
                if (response.code() == 200) {
                    tagsByCartridgeLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    tagsByCartridgeLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseTagsByCartridgeDto>, t: Throwable) {
                tagsByCartridgeLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 015. 추천태그별 카트리지 리스트를 가져온다
    fun requestCartridgeListByTag(requestCartridgeListByTagDto: RequestCartridgeListByTagDto) {
        arPangInterface.postCartridgeListByTags(
            requestCartridgeListByTagDto.tag_seq_no,
            requestCartridgeListByTagDto.app_id,
            requestCartridgeListByTagDto.menu_code,
            requestCartridgeListByTagDto.currPage,
            requestCartridgeListByTagDto.pageSize
        ).enqueue(object : Callback<ResponseCartridgeListByTagDto> {
            override fun onResponse(
                call: Call<ResponseCartridgeListByTagDto>,
                response: Response<ResponseCartridgeListByTagDto>
            ) {
                if (response.code() == 200) {
                    cartridgeListByTagsLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    cartridgeListByTagsLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseCartridgeListByTagDto>, t: Throwable) {
                cartridgeListByTagsLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 016. 사용자 알림 설정
    fun requestUserAlertSetting(requestUserAlertSettingDto: RequestUserAlertSettingDto) {
        arPangInterface.postUserAlertSetting(
            requestUserAlertSettingDto.user_id,
            requestUserAlertSettingDto.noti_event_se,
            requestUserAlertSettingDto.subscrip_se,
            requestUserAlertSettingDto.favor_se,
            requestUserAlertSettingDto.comment_se
        ).enqueue(object : Callback<ResponseUserAlertSettingDto> {
            override fun onResponse(
                call: Call<ResponseUserAlertSettingDto>,
                response: Response<ResponseUserAlertSettingDto>
            ) {
                if (response.code() == 200) {
                    userAlertSettingLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    userAlertSettingLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseUserAlertSettingDto>, t: Throwable) {
                userAlertSettingLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 017. 사용자 알림 리스트
    fun requestUserAlertList(requestUserAlertListDto: RequestUserAlertListDto) {
        arPangInterface.postUserAlertList(
            requestUserAlertListDto.user_id
        ).enqueue(object : Callback<ResponseUserAlertListDto> {
            override fun onResponse(
                call: Call<ResponseUserAlertListDto>,
                response: Response<ResponseUserAlertListDto>
            ) {
                if (response.code() == 200) {
                    userAlertListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    userAlertListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseUserAlertListDto>, t: Throwable) {
                userAlertListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 018. 템플릿 리스트
    fun requestTemplateList(requestTemplateListDto: RequestTemplateListDto) {
        arPangInterface.postTemplateList(
            requestTemplateListDto.ctgy_se,
            requestTemplateListDto.ctgy_code_step1,
            requestTemplateListDto.version_no_start,
            requestTemplateListDto.version_no_end,
            requestTemplateListDto.character_brand,
            requestTemplateListDto.currPage,
            requestTemplateListDto.pageSize
        ).enqueue(object : Callback<ResponseTemplateListDto> {
            override fun onResponse(
                call: Call<ResponseTemplateListDto>,
                response: Response<ResponseTemplateListDto>
            ) {
                if (response.code() == 200) {
                    templateListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    templateListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseTemplateListDto>, t: Throwable) {
                templateListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 019. 금지 해시태그조회
    fun requestIgnoreTagCheck(requestIgnoreTagCheckDto: RequestIgnoreTagCheckDto) {
        arPangInterface.postIgnoreTagCheck(
            requestIgnoreTagCheckDto.share_hash_tag
        ).enqueue(object : Callback<ResponseIgnoreTagCheckDto> {
            override fun onResponse(
                call: Call<ResponseIgnoreTagCheckDto>,
                response: Response<ResponseIgnoreTagCheckDto>
            ) {
                if (response.code() == 200) {
                    ignoreTagCheckLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    ignoreTagCheckLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseIgnoreTagCheckDto>, t: Throwable) {
                ignoreTagCheckLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 020. 에디터 Object 저장(일괄)
    fun requestSaveEditor(requestSaveEditorDto: RequestSaveEditorDto) {
        arPangInterface.postSaveEditor(
            requestSaveEditorDto.user_id,
            requestSaveEditorDto.ctge_no,
            requestSaveEditorDto.main_image,
            requestSaveEditorDto.editor_data,
            requestSaveEditorDto.pixel_x,
            requestSaveEditorDto.share_hash_tag,
            requestSaveEditorDto.share_se,
            requestSaveEditorDto.menu_code,
            requestSaveEditorDto.parent_cntnts_no
        ).enqueue(object : Callback<ResponseSaveEditorDto> {
            override fun onResponse(
                call: Call<ResponseSaveEditorDto>,
                response: Response<ResponseSaveEditorDto>
            ) {
                if (response.code() == 200) {
                    saveEditorLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    saveEditorLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseSaveEditorDto>, t: Throwable) {
                saveEditorLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 021. 보관함(사용자) 저장 컨텐츠 리스트
    fun requestStorageContentList(requestStorageContentListDto: RequestStorageContentListDto) {
        arPangInterface.postStorageContentList(
            requestStorageContentListDto.user_id,
            requestStorageContentListDto.currPage,
            requestStorageContentListDto.pageSize,
            requestStorageContentListDto.hash_tag,
            requestStorageContentListDto.menu_code,
            requestStorageContentListDto.ctge_no,
            requestStorageContentListDto.share_se
        ).enqueue(object : Callback<ResponseStorageContentListDto> {
            override fun onResponse(
                call: Call<ResponseStorageContentListDto>,
                response: Response<ResponseStorageContentListDto>
            ) {
                if (response.code() == 200) {
                    storageContentListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    storageContentListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseStorageContentListDto>, t: Throwable) {
                storageContentListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 022. 보관함(사용자) 저장 컨텐츠 상세조회
    fun requestStorageContentDetail(requestStorageDetailDto: RequestStorageDetailDto) {
        arPangInterface.postStorageContentDetail(
            requestStorageDetailDto.cntnts_no,
            requestStorageDetailDto.user_id
        ).enqueue(object : Callback<ResponseStorageDetailDto> {
            override fun onResponse(
                call: Call<ResponseStorageDetailDto>,
                response: Response<ResponseStorageDetailDto>
            ) {
                if (response.code() == 200) {
                    storageContentDetailDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    storageContentDetailDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseStorageDetailDto>, t: Throwable) {
                storageContentDetailDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 023. 사용자가 공유(공개)한 컨텐츠 리스트
    fun requestShareContentUserOpenList(requestShareContentUserOpenListDto: RequestShareContentUserOpenListDto) {
        arPangInterface.postShareContentUserOpenList(
            requestShareContentUserOpenListDto.user_id,
            requestShareContentUserOpenListDto.currPage,
            requestShareContentUserOpenListDto.pageSize,
            requestShareContentUserOpenListDto.hash_tag,
            requestShareContentUserOpenListDto.menu_code
        ).enqueue(object : Callback<ResponseShareContentUserOpenListDto> {
            override fun onResponse(
                call: Call<ResponseShareContentUserOpenListDto>,
                response: Response<ResponseShareContentUserOpenListDto>
            ) {
                if (response.code() == 200) {
                    shareContentUserOpenListLiveDataRepository.postValue(
                        NetworkResult.Success(
                            response.body()
                        )
                    )
                } else {
                    shareContentUserOpenListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseShareContentUserOpenListDto>, t: Throwable) {
                shareContentUserOpenListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 024. 전체 공유(공개) 컨텐츠 리스트
    fun requestShareContentAllOpenList(requestShareContentAllOpenListDto: RequestShareContentAllOpenListDto) {
        arPangInterface.postShareContentAllOpenList(
            requestShareContentAllOpenListDto.currPage,
            requestShareContentAllOpenListDto.pageSize,
            requestShareContentAllOpenListDto.hash_tag,
            requestShareContentAllOpenListDto.menu_code
        ).enqueue(object : Callback<ResponseShareContentAllOpenListDto> {
            override fun onResponse(
                call: Call<ResponseShareContentAllOpenListDto>,
                response: Response<ResponseShareContentAllOpenListDto>
            ) {
                if (response.code() == 200) {
                    shareContentAllOpenListLiveDataRepository.postValue(
                        NetworkResult.Success(
                            response.body()
                        )
                    )
                } else {
                    shareContentAllOpenListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseShareContentAllOpenListDto>, t: Throwable) {
                shareContentAllOpenListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 025. 공개(공유)된 컨텐츠 상세조회
    fun requestShareContentDetail(requestShareDetailDto: RequestShareDetailDto) {
        arPangInterface.postShareDetailContent(
            requestShareDetailDto.cntnts_no,
            requestShareDetailDto.user_id
        ).enqueue(object : Callback<ResponseShareDetailDto> {
            override fun onResponse(
                call: Call<ResponseShareDetailDto>,
                response: Response<ResponseShareDetailDto>
            ) {
                if (response.code() == 200) {
                    shareDetailLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    shareDetailLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseShareDetailDto>, t: Throwable) {
                shareDetailLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 026. 전체 인기있는 해시태그 리스트(상위5건)
    fun requestAllFavoriteHashTag(requestAllFavoriteDto: RequestAllFavoriteDto) {
        arPangInterface.postAllFavoriteHashTag(
            requestAllFavoriteDto.search_nm
        ).enqueue(object : Callback<ResponseAllFavoriteDto> {
            override fun onResponse(
                call: Call<ResponseAllFavoriteDto>,
                response: Response<ResponseAllFavoriteDto>
            ) {
                if (response.code() == 200) {
                    allFavoriteHashTagLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    allFavoriteHashTagLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseAllFavoriteDto>, t: Throwable) {
                allFavoriteHashTagLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 027. 내가등록한 해시태그 리스트 가져오기 (상위5건)
    fun requestMyFavoriteHashTag(requestMyFavoriteDto: RequestMyFavoriteDto) {
        arPangInterface.postMyFavoriteHashTag(
            requestMyFavoriteDto.search_nm
        ).enqueue(object : Callback<ResponseMyFavoriteDto> {
            override fun onResponse(
                call: Call<ResponseMyFavoriteDto>,
                response: Response<ResponseMyFavoriteDto>
            ) {
                if (response.code() == 200) {
                    myFavoriteHashTagLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    myFavoriteHashTagLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseMyFavoriteDto>, t: Throwable) {
                myFavoriteHashTagLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 028. 댓글 리스트
    fun requestCommentList(requestCommentListDto: RequestCommentListDto) {
        arPangInterface.postCommentList(
            requestCommentListDto.cntnts_no,
            requestCommentListDto.user_id,
            requestCommentListDto.currPage,
            requestCommentListDto.pageSize
        ).enqueue(object : Callback<ResponseCommentListDto> {
            override fun onResponse(
                call: Call<ResponseCommentListDto>,
                response: Response<ResponseCommentListDto>
            ) {
                if (response.code() == 200) {
                    commentListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    commentListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseCommentListDto>, t: Throwable) {
                commentListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 029. 댓글 작성
    fun requestAddComment(requestAddCommentDto: RequestAddCommentDto) {
        arPangInterface.postAddComment(
            requestAddCommentDto.cntnts_no,
            requestAddCommentDto.user_id,
            requestAddCommentDto.comment
        ).enqueue(object : Callback<ResponseAddCommentDto> {
            override fun onResponse(
                call: Call<ResponseAddCommentDto>,
                response: Response<ResponseAddCommentDto>
            ) {
                if (response.code() == 200) {
                    addCommentLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    addCommentLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseAddCommentDto>, t: Throwable) {
                addCommentLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 030. 댓글 삭제
    fun requestDelComment(requestDelCommentDto: RequestDelCommentDto) {
        arPangInterface.postDelComment(
            requestDelCommentDto.cntnts_no,
            requestDelCommentDto.user_id,
            requestDelCommentDto.seq_no
        ).enqueue(object : Callback<ResponseDelCommentDto> {
            override fun onResponse(
                call: Call<ResponseDelCommentDto>,
                response: Response<ResponseDelCommentDto>
            ) {
                if (response.code() == 200) {
                    delCommentLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    delCommentLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseDelCommentDto>, t: Throwable) {
                delCommentLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 031. 상세컨텐츠의 해시태그 리스트
    fun requestDetailHashTagList(requestDetailContentHashTagDto: RequestDetailContentHashTagDto) {
        arPangInterface.postDetailHashTagList(
            requestDetailContentHashTagDto.cntnts_no
        ).enqueue(object : Callback<ResponseDetailContentHashTagDto> {
            override fun onResponse(
                call: Call<ResponseDetailContentHashTagDto>,
                response: Response<ResponseDetailContentHashTagDto>
            ) {
                if (response.code() == 200) {
                    detailContentHashListRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    detailContentHashListRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseDetailContentHashTagDto>, t: Throwable) {
                detailContentHashListRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 032. 컨텐츠 공개여부 설정하기
    fun requestPublicContentSetting(requestPublicContentSettingDto: RequestPublicContentSettingDto) {
        arPangInterface.postPublicContentSetting(
            requestPublicContentSettingDto.cntnts_no,
            requestPublicContentSettingDto.share_se,
            requestPublicContentSettingDto.menu_code,
            requestPublicContentSettingDto.user_id
        ).enqueue(object : Callback<ResponsePublicContentSettingDto> {
            override fun onResponse(
                call: Call<ResponsePublicContentSettingDto>,
                response: Response<ResponsePublicContentSettingDto>
            ) {
                if (response.code() == 200) {
                    publicContentSettingLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    publicContentSettingLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponsePublicContentSettingDto>, t: Throwable) {
                publicContentSettingLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 033-1. 구독하기 등록/해제
    fun requestSubscribeUpdate(requestSubscribeUpdateDto: RequestSubscribeUpdateDto) {
        arPangInterface.postSubscribeReg(
            requestSubscribeUpdateDto.user_id,
            requestSubscribeUpdateDto.sub_user_id,
            requestSubscribeUpdateDto.subscription_se
        ).enqueue(object : Callback<ResponseSubscribeUpdateDto> {
            override fun onResponse(
                call: Call<ResponseSubscribeUpdateDto>,
                response: Response<ResponseSubscribeUpdateDto>
            ) {
                if (response.code() == 200) {
                    subscribeUpdateLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    subscribeUpdateLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseSubscribeUpdateDto>, t: Throwable) {
                subscribeUpdateLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 033-2. 내가 구독한 사용자 목록
    fun requestSubscribeListByMy(requestSubscribeByMyDto: RequestSubscribeByMyDto) {
        arPangInterface.postSubscribeListByMy(
            requestSubscribeByMyDto.user_id,
            requestSubscribeByMyDto.currPage,
            requestSubscribeByMyDto.pageSize
        ).enqueue(object : Callback<ResponseSubscribeListDto> {
            override fun onResponse(
                call: Call<ResponseSubscribeListDto>,
                response: Response<ResponseSubscribeListDto>
            ) {
                if (response.code() == 200) {
                    subscribeListByMyLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    subscribeListByMyLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseSubscribeListDto>, t: Throwable) {
                subscribeListByMyLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 033-3. 나를 구독한 사용자 목록
    fun requestSubscribeListByMe(requestSubscribeByMeDto: RequestSubscribeByMeDto) {
        arPangInterface.postSubscribeListByMe(
            requestSubscribeByMeDto.user_id,
            requestSubscribeByMeDto.currPage,
            requestSubscribeByMeDto.pageSize
        ).enqueue(object : Callback<ResponseSubscribeListDto> {
            override fun onResponse(
                call: Call<ResponseSubscribeListDto>,
                response: Response<ResponseSubscribeListDto>
            ) {
                if (response.code() == 200) {
                    subscribeListByMeLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    subscribeListByMeLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseSubscribeListDto>, t: Throwable) {
                subscribeListByMeLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 033-5. 구독자 전체 갯수 조회
    fun requestSubscribeTotalCount(requestSubscribeTotalDto: RequestSubscribeTotalDto) {
        arPangInterface.postSubscribeTotalCount(
            requestSubscribeTotalDto.user_id
        ).enqueue(object : Callback<ResponseSubscribeTotalDto> {
            override fun onResponse(
                call: Call<ResponseSubscribeTotalDto>,
                response: Response<ResponseSubscribeTotalDto>
            ) {
                if (response.code() == 200) {
                    subscribeTotalCntLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    subscribeTotalCntLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseSubscribeTotalDto>, t: Throwable) {
                subscribeTotalCntLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 034. 상세컨텐츠의 해시태그 리스트 편집
    fun requestEditContentHashTag(requestEditContentHashTagDto: RequestEditContentHashTagDto) {
        arPangInterface.postEditContentHashTag(
            requestEditContentHashTagDto.cntnts_no,
            requestEditContentHashTagDto.user_id,
            requestEditContentHashTagDto.share_hash_tag
        ).enqueue(object : Callback<ResponseEditContentHashTagDto> {
            override fun onResponse(
                call: Call<ResponseEditContentHashTagDto>,
                response: Response<ResponseEditContentHashTagDto>
            ) {
                if (response.code() == 200) {
                    editContentHashTagLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    editContentHashTagLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseEditContentHashTagDto>, t: Throwable) {
                editContentHashTagLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 033-5. 구독자 전체 갯수 조회


    //TODO 035. 보관함 컨텐츠 삭제
    fun requestDeleteStorageContent(requestDeleteStorageDto: RequestDeleteStorageDto) {
        arPangInterface.postDeleteStorageContent(
            requestDeleteStorageDto.cntnts_no,
            requestDeleteStorageDto.user_id
        ).enqueue(object : Callback<ResponseDeleteStorageDto> {
            override fun onResponse(
                call: Call<ResponseDeleteStorageDto>,
                response: Response<ResponseDeleteStorageDto>
            ) {
                if (response.code() == 200) {
                    storageContentDeleteLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    storageContentDeleteLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseDeleteStorageDto>, t: Throwable) {
                storageContentDeleteLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 036. 댓글신고(다른사람이 쓴 댓글)
    fun requestCommentReport(requestCommentReportDto: RequestCommentReportDto) {
        arPangInterface.postCommentReport(
            requestCommentReportDto.cntnts_no,
            requestCommentReportDto.comment_seq_no,
            requestCommentReportDto.user_id,
            requestCommentReportDto.write_user_id,
            requestCommentReportDto.complain_desc
        ).enqueue(object : Callback<ResponseCommentReportDto> {
            override fun onResponse(
                call: Call<ResponseCommentReportDto>,
                response: Response<ResponseCommentReportDto>
            ) {
                if (response.code() == 200) {
                    commentReportLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    commentReportLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseCommentReportDto>, t: Throwable) {
                commentReportLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }

        })
    }

    //TODO 037. 구독한 사용자의 공유(공개)한 컨텐츠 리스트
    fun requestSubscribeUserContentList(requestSubscribeUserContentListDto: RequestSubscribeUserContentListDto) {
        arPangInterface.postSubscribeUserContentList(
            requestSubscribeUserContentListDto.sub_user_id,
            requestSubscribeUserContentListDto.currPage,
            requestSubscribeUserContentListDto.pageSize,
            requestSubscribeUserContentListDto.hash_tag,
            requestSubscribeUserContentListDto.menu_code
        ).enqueue(object : Callback<ResponseSubscribeUserContentListDto> {
            override fun onResponse(
                call: Call<ResponseSubscribeUserContentListDto>,
                response: Response<ResponseSubscribeUserContentListDto>
            ) {
                if (response.code() == 200) {
                    subscribeUserContentListLiveDataRepository.postValue(
                        NetworkResult.Success(
                            response.body()
                        )
                    )
                } else {
                    subscribeUserContentListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseSubscribeUserContentListDto>, t: Throwable) {
                subscribeUserContentListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 038. 좋아요 등록
    fun requestFavoriteUpdate(requestFavoriteDto: RequestFavoriteDto) {
        arPangInterface.postFavoriteUpdate(
            requestFavoriteDto.user_id,
            requestFavoriteDto.cntnts_no,
            requestFavoriteDto.favorite_se
        ).enqueue(object : Callback<ResponseFavoriteDto> {
            override fun onResponse(
                call: Call<ResponseFavoriteDto>,
                response: Response<ResponseFavoriteDto>
            ) {
                if (response.code() == 200) {
                    favoriteUpdateLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    favoriteUpdateLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseFavoriteDto>, t: Throwable) {
                favoriteUpdateLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 039. 공유공간 상세 게시글을 신고
    fun requestContentReport(requestContentReportDto: RequestContentReportDto) {
        arPangInterface.postContentReport(
            requestContentReportDto.cntnts_no,
            requestContentReportDto.user_id,
            requestContentReportDto.write_user_id,
            requestContentReportDto.complain_desc
        ).enqueue(object : Callback<ResponseContentReportDto> {
            override fun onResponse(
                call: Call<ResponseContentReportDto>,
                response: Response<ResponseContentReportDto>
            ) {
                if (response.code() == 200) {
                    contentReportLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    contentReportLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseContentReportDto>, t: Throwable) {
                contentReportLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 040. 스크랩 등록/삭제
    fun requestScrapUpdate(requestScrapUpdateDto: RequestScrapUpdateDto) {
        arPangInterface.postScrapUpdate(
            requestScrapUpdateDto.user_id,
            requestScrapUpdateDto.cntnts_no,
            requestScrapUpdateDto.scrap_se
        ).enqueue(object : Callback<ResponseScrapUpdateDto> {
            override fun onResponse(
                call: Call<ResponseScrapUpdateDto>,
                response: Response<ResponseScrapUpdateDto>
            ) {
                if (response.code() == 200) {
                    scrapUpdateLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    scrapUpdateLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseScrapUpdateDto>, t: Throwable) {
                scrapUpdateLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 041. 스크랩 컨텐츠 리스트
    fun requestScrapContentList(requestScrapContentDto: RequestScrapContentDto) {
        arPangInterface.postScrapContentList(
            requestScrapContentDto.user_id,
            requestScrapContentDto.currPage,
            requestScrapContentDto.pageSize,
            requestScrapContentDto.hash_tag,
            requestScrapContentDto.menu_code,
            requestScrapContentDto.ctge_no
        ).enqueue(object : Callback<ResponseScrapContentDto> {
            override fun onResponse(
                call: Call<ResponseScrapContentDto>,
                response: Response<ResponseScrapContentDto>
            ) {
                if (response.code() == 200) {
                    scrapContentListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    scrapContentListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseScrapContentDto>, t: Throwable) {
                scrapContentListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 042. 특정 사용자 신고하기
    fun requestTargetUserReport(requestTargetUserReportDto: RequestTargetUserReportDto) {
        arPangInterface.postTargetUserReport(
            requestTargetUserReportDto.user_id,
            requestTargetUserReportDto.complain_user_id,
            requestTargetUserReportDto.complain_desc
        ).enqueue(object : Callback<ResponseTargetUserReportDto> {
            override fun onResponse(
                call: Call<ResponseTargetUserReportDto>,
                response: Response<ResponseTargetUserReportDto>
            ) {
                if (response.code() == 200) {
                    targetUserReportLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    targetUserReportLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseTargetUserReportDto>, t: Throwable) {
                targetUserReportLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 043. 특정 사용자 차단 등록/해제
    fun requestTargetUserBlock(requestTargetUserBlockDto: RequestTargetUserBlockDto) {
        arPangInterface.postTargetUserBlock(
            requestTargetUserBlockDto.user_id,
            requestTargetUserBlockDto.block_user_id,
            requestTargetUserBlockDto.block_se
        ).enqueue(object : Callback<ResponseTargetUserBlockDto> {
            override fun onResponse(
                call: Call<ResponseTargetUserBlockDto>,
                response: Response<ResponseTargetUserBlockDto>
            ) {
                if (response.code() == 200) {
                    targetUserBlockUpdateLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    targetUserBlockUpdateLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseTargetUserBlockDto>, t: Throwable) {
                targetUserBlockUpdateLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 044. 차단한 사용자 목록
    fun requestBlockUserList(requestBlockUserListDto: RequestBlockUserListDto) {
        arPangInterface.postBlockUserList(
            requestBlockUserListDto.user_id,
            requestBlockUserListDto.currPage,
            requestBlockUserListDto.pageSize
        ).enqueue(object : Callback<ResponseBlockUserListDto> {
            override fun onResponse(
                call: Call<ResponseBlockUserListDto>,
                response: Response<ResponseBlockUserListDto>
            ) {
                if (response.code() == 200) {
                    blockUserListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    blockUserListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseBlockUserListDto>, t: Throwable) {
                blockUserListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 045. 공지사항 리스트
    fun requestNoticeList(requestNoticeListDto: RequestNoticeListDto) {
        arPangInterface.postNoticeList(
            requestNoticeListDto.app_id,
            requestNoticeListDto.lang_code
        ).enqueue(object : Callback<ResponseNoticeListDto> {
            override fun onResponse(
                call: Call<ResponseNoticeListDto>,
                response: Response<ResponseNoticeListDto>
            ) {
                if (response.code() == 200) {
                    noticeListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    noticeListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseNoticeListDto>, t: Throwable) {
                noticeListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 046. 공지사항 상세보기
    fun requestNoticeDetail(requestNoticeDetailDto: RequestNoticeDetailDto) {
        arPangInterface.postNoticeDetail(
            requestNoticeDetailDto.bbsid
        ).enqueue(object : Callback<ResponseNoticeDetailDto> {
            override fun onResponse(
                call: Call<ResponseNoticeDetailDto>,
                response: Response<ResponseNoticeDetailDto>
            ) {
                if (response.code() == 200) {
                    noticeDetailLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    noticeDetailLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseNoticeDetailDto>, t: Throwable) {
                noticeDetailLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 047. 인기 검색 태그
    fun requestPopularTag() {
        arPangInterface.postPopularTag()
            .enqueue(object : Callback<ResponsePopularTagDto> {
                override fun onResponse(
                    call: Call<ResponsePopularTagDto>,
                    response: Response<ResponsePopularTagDto>
                ) {
                    if (response.code() == 200) {
                        popularTagSearchLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                    } else {
                        popularTagSearchLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<ResponsePopularTagDto>, t: Throwable) {
                    popularTagSearchLiveDataRepository.postValue(NetworkResult.Error(t.message))
                }
            })
    }

    //TODO 048. 입력된 단어가 포함된 이름/공유/보관함 검색중 리스트
    fun requestSearchingInputMatch(requestSearchingMatchDto: RequestSearchingMatchDto) {
        arPangInterface.postSearchingInputMatch(
            requestSearchingMatchDto.user_id,
            requestSearchingMatchDto.search_nm
        ).enqueue(object : Callback<ResponseSearchingMatchDto> {
            override fun onResponse(
                call: Call<ResponseSearchingMatchDto>,
                response: Response<ResponseSearchingMatchDto>
            ) {
                if (response.code() == 200) {
                    searchingMatchListLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    searchingMatchListLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseSearchingMatchDto>, t: Throwable) {
                searchingMatchListLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 049. 입력된 단어가 포함된 이름/공유/보관함 검색결과 리스트
    fun requestResultMatch(requestResultMatchDto: RequestResultMatchDto) {
        arPangInterface.postResultMatch(
            requestResultMatchDto.user_id,
            requestResultMatchDto.search_nm,
            requestResultMatchDto.currPage,
            requestResultMatchDto.pageSize
        ).enqueue(object : Callback<ResponseResultMatchDto> {
            override fun onResponse(
                call: Call<ResponseResultMatchDto>,
                response: Response<ResponseResultMatchDto>
            ) {
                if (response.code() == 200) {
                    resultMatchingLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    resultMatchingLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseResultMatchDto>, t: Throwable) {
                resultMatchingLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }

    //TODO 050. 메인베너, 팝업베너, 이렇게활용 리스트
    fun requestMainBanner(requestMainBannerDto: RequestMainBannerDto) {
        arPangInterface.postMainBanner(
            requestMainBannerDto.banner_se_code,
            requestMainBannerDto.menu_code
        ).enqueue(object : Callback<ResponseMainBannerDto> {
            override fun onResponse(
                call: Call<ResponseMainBannerDto>,
                response: Response<ResponseMainBannerDto>
            ) {
                if (response.code() == 200) {
                    mainBannerLiveDataRepository.postValue(NetworkResult.Success(response.body()))
                } else {
                    mainBannerLiveDataRepository.postValue(NetworkResult.NetCode("${response.code()}"))
                }
            }

            override fun onFailure(call: Call<ResponseMainBannerDto>, t: Throwable) {
                mainBannerLiveDataRepository.postValue(NetworkResult.Error(t.message))
            }
        })
    }
}