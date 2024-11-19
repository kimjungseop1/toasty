package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLoginDto {
    @SerializedName("msgCode")
    @Expose
    var msgCode: String = ""

    @SerializedName("mobl_no")
    @Expose
    var mobl_no: String? = null // 전화번호

    @SerializedName("email")
    @Expose
    var email: String? = null // 이메일

    @SerializedName("nick_nm")
    @Expose
    var nick_nm: String? = null // 닉네임

    @SerializedName("stt_msg")
    @Expose
    var stt_msg: String? = null // 상태메시지

    @SerializedName("profile_pic_path")
    @Expose
    var profile_pic_path: String? = null // 프로필사진 url

    @SerializedName("nation")
    @Expose
    var nation: String? = null // 국가

    @SerializedName("lang")
    @Expose
    var lang: String? = null // 언어

    @SerializedName("birth_day")
    @Expose
    var birth_day: String? = null // 생일

    @SerializedName("login_connt_accnt")
    @Expose
    var login_connt_accnt: String? = null // 플랫폼

    @SerializedName("Authorization")
    @Expose
    var Authorization: String? = null

    @SerializedName("Re-Authorization")
    @Expose
    var Re_Authorization: String? = null
}