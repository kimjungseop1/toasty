package com.syncrown.arpang.network

import com.syncrown.arpang.network.model.ResponseAppMainDto
import com.syncrown.arpang.network.model.ResponseAppMenuByCartridgeDto
import com.syncrown.arpang.network.model.ResponseCartridgeByAppMenuDto
import com.syncrown.arpang.network.model.ResponseCartridgeListDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseCheckNickNameDto
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.network.model.ResponseTagsByCartridgeDto
import com.syncrown.arpang.network.model.ResponseUpdateProfileDto
import com.syncrown.arpang.network.model.ResponseUserProfileDto
import com.syncrown.arpang.network.model.ResponseUserTokenDelDto
import com.syncrown.arpang.network.model.ResponseUserTokenRegDto
import com.syncrown.arpang.network.model.ResponseWithdrawalDto
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ArPangInterface {
    //TODO 001. 사용자 토큰 등록
    @FormUrlEncoded
    @POST("/ntv/insert/user/token")
    fun postInsertUserToken(
        @Field("user_token") user_token: String,
        @Field("app_id") app_id: String,
        @Field("device_id") device_id: String,
        @Field("user_id") user_id: String?
    ): Call<ResponseUserTokenRegDto>

    //TODO 002. 사용자 토큰 삭제
    @FormUrlEncoded
    @POST("/ntv/delete/user/token")
    fun postDeleteUserToken(
        @Field("app_id") app_id: String,
        @Field("device_id") device_id: String
    ): Call<ResponseUserTokenDelDto>

    //TODO 003. 회원여부 확인
    @FormUrlEncoded
    @POST("/ntv/member/check/app")
    fun postCheckMember(
        @Field("user_id") user_id: String
    ): Call<ResponseCheckMember>

    //TODO 004. 가입신청
    @FormUrlEncoded
    @POST("/ntv/join/req/app")
    fun postJoin(
        @Field("user_id") user_id: String,
        @Field("login_connt_accnt") login_connt_accnt: String,
        @Field("userid_se") userid_se: Int,
        @Field("user_nm") user_nm: String?,
        @Field("use_se") use_se: Int,
        @Field("mobl_no") mobl_no: String?,
        @Field("email") email: String?,
        @Field("nick_nm") nick_nm: String?,
        @Field("stt_msg") stt_msg: String?,
        @Field("nation") nation: String?,
        @Field("lang") lang: String?,
        @Field("birth_day") birth_day: String?,
        @Field("markt_recptn_agre") markt_recptn_agre: Int?,
        @Field("profile") profile: String?
    ): Call<ResponseJoinDto>

    //TODO 005. 로그인
    @FormUrlEncoded
    @POST("/ntv/login/app")
    fun postLogin(
        @Field("user_id") user_id: String
    ): Call<ResponseLoginDto>

    //TODO 006. 프로필 수정
    @FormUrlEncoded
    @POST("/ntv/atp/user/update")
    fun postUpdateProfile(
        @Field("user_id") user_id: String,
        @Field("nick_nm") nick_nm: String?,
        @Field("mobl_no") mobl_no: String?,
        @Field("stt_msg") stt_msg: String?,
        @Field("lang") lang: String?,
        @Field("profile") profile: String?
    ): Call<ResponseUpdateProfileDto>

    //TODO 006-1. 사용자 정보 조회
    @FormUrlEncoded
    @POST("/ntv/atp/user/get")
    fun postUserProfile(
        @Field("user_id") user_id: String
    ): Call<ResponseUserProfileDto>

    //TODO 007. 별명 중복 체크
    @FormUrlEncoded
    @POST("/ntv/atp/nick/duple")
    fun postCheckNickName(
        @Field("user_id") user_id: String,
        @Field("nick_nm") nick_nm: String
    ): Call<ResponseCheckNickNameDto>

    //TODO 008. 회원탈퇴
    @FormUrlEncoded
    @POST("/ntv/atp/member/break")
    fun postWithdrawal(
        @Field("user_id") user_id: String,
        @Field("break_reson") break_reson: String?,
        @Field("etc_reson") etc_reson: String?
    ): Call<ResponseWithdrawalDto>

    //TODO 009.


    //TODO 010. 앱메인메뉴 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/appmenu")
    fun postAppMain(
        @Field("app_id") app_id: String
    ): Call<ResponseAppMainDto>

    //TODO 011. 카트리지 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/cartridge/list")
    fun postCartridgeList(
        @Field("app_id") app_id: String,
        @Field("device_os") device_os: String,
        @Field("ctge_no") ctge_no: Int?,
        @Field("ctge_nm") ctge_nm: String?,
        @Field("ctge_model") ctge_model: String?,
        @Field("ctge_model_abbr") ctge_model_abbr: String?,
        @Field("character_brand") character_brand: String?
    ): Call<ResponseCartridgeListDto>

    //TODO 012. 카트리지별 앱메뉴 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/cartridge/appmenu/list")
    fun postAppMenuByCartridge(
        @Field("ctge_no") ctge_no: Int,
        @Field("app_id") app_id: String
    ): Call<ResponseAppMenuByCartridgeDto>

    //TODO 013. 앱메뉴별 카트리지 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/appmenu/cartridge/list")
    fun postCartridgeByAppMenu(
        @Field("menu_code") menu_code: String,
        @Field("app_id") app_id: String
    ): Call<ResponseCartridgeByAppMenuDto>

    //TODO 014. 카트리지별 추천태그 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/cartridge/tag/recommend/list")
    fun postTagsByCartridge(
        @Field("ctge_no") ctge_no: Int,
        @Field("app_id") app_id: String
    ): Call<ResponseTagsByCartridgeDto>
}