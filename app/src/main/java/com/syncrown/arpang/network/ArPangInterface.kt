package com.syncrown.arpang.network

import com.syncrown.arpang.network.model.RequestCheckMember
import com.syncrown.arpang.network.model.RequestJoinDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseJoinDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ArPangInterface {
    //TODO 회원여부 확인
    @POST("/ntv/member/check")
    fun postCheckMember(
        @Body requestCheckMember: RequestCheckMember
    ): Call<ResponseCheckMember>

    //TODO 가입신청
    @POST("/ntv/join/req")
    fun postJoin(
        @Body requestJoinDto: RequestJoinDto
    ): Call<ResponseJoinDto>
}