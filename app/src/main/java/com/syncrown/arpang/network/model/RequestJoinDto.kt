package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestJoinDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = "" // 사용자 아이디 로그인접속사이트+'-'+계정

    @SerializedName("login_connt_accnt")
    @Expose
    var login_connt_accnt: String = "" // 로그인 접속 사이트 NAVER(n), FACEBOOK(f),GOOGLE(g),KAKAO(k),INSTAGRAM(i)

    @SerializedName("userid_se")
    @Expose
    var userid_se: Int = -1 // 사용자 id 구분 이메일:1,폰:0

    @SerializedName("user_nm")
    @Expose
    var user_nm: String? = null // 사용자명

    @SerializedName("use_se")
    @Expose
    var use_se: Int = -1 // 가입구분 가입: 1, 탈퇴: 9

    @SerializedName("mobl_no")
    @Expose
    var mobl_no: String? = null // 휴대전화 번호

    @SerializedName("email")
    @Expose
    var email: String? = null // 이메일

    @SerializedName("nick_nm")
    @Expose
    var nick_nm: String? = null // 닉네임

    @SerializedName("stt_msg")
    @Expose
    var stt_msg: String? = null // 상태메시지

    @SerializedName("nation")
    @Expose
    var nation: String? = null // 가입국가

    @SerializedName("lang")
    @Expose
    var lang: String? = null // 언어

    @SerializedName("birth_day")
    @Expose
    var birth_day: String? = null // 생일

    @SerializedName("markt_recptn_agre")
    @Expose
    var markt_recptn_agre: Int? = null // 마케팅 수신 동의 여부 동의 : 1, 미동의 : 0

    @SerializedName("profile")
    @Expose
    var profile: String? = null // 프로필사진 Base64 변환값

    @SerializedName("app_id")
    @Expose
    var app_id: String = ""
}