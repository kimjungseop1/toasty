package com.syncrown.arpang.network

import com.syncrown.arpang.AppDataPref
import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseCheckNickNameDto
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.network.model.ResponseUpdateProfileDto
import com.syncrown.arpang.network.model.ResponseUserTokenDelDto
import com.syncrown.arpang.network.model.ResponseUserTokenRegDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
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

    //TODO 007. 별명 중복 체크
    @FormUrlEncoded
    @POST("/ntv/atp/nick/duple")
    fun postCheckNickName(
        @Field("nick_nm") nick_nm: String
    ): Call<ResponseCheckNickNameDto>
}