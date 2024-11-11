package com.syncrown.arpang.network

import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.RequestNoticeListDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.network.model.ResponseNoticeListDto
import com.syncrown.arpang.network.model.ResponseUserTokenDelDto
import com.syncrown.arpang.network.model.ResponseUserTokenRegDto
import retrofit2.Call
import retrofit2.http.Body
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
    @POST("/ntv/member/check")
    fun postCheckMember(
        @Body requestCheckMember: RequestCheckMember
    ): Call<ResponseCheckMember>

    //TODO 004. 가입신청
    @POST("/ntv/join/req")
    fun postJoin(
        @Body requestJoinDto: RequestJoinDto
    ): Call<ResponseJoinDto>

    //TODO 005. 로그인
    @POST("/ntv/login")
    fun postLogin(
        @Body requestLoginDto: RequestLoginDto
    ): Call<ResponseLoginDto>

    //TODO 공지사항 리스트
    @POST("/ntv/list/notice")
    fun postNoticeList(
        @Body requestNoticeList: RequestNoticeListDto
    ): Call<ResponseNoticeListDto>
}