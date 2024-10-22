package com.syncrown.arpang.network

import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.RequestLoginDto
import com.syncrown.arpang.network.model.RequestNoticeListDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.network.model.ResponseNoticeListDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ArPangInterface {
    //TODO 001. 회원여부 확인
    @POST("/ntv/member/check")
    fun postCheckMember(
        @Body requestCheckMember: RequestCheckMember
    ): Call<ResponseCheckMember>

    //TODO 002. 가입신청
    @POST("/ntv/join/req")
    fun postJoin(
        @Body requestJoinDto: RequestJoinDto
    ): Call<ResponseJoinDto>

    //TODO 003. 로그인
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