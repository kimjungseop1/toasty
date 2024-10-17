package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLoginDto {
    @SerializedName("msgCode")
    @Expose
    val msgCode: String = ""

    @SerializedName("mobl_no")
    @Expose
    val mobl_no: String? = null

    @SerializedName("email")
    @Expose
    val email: String? = null

    @SerializedName("nick_nm")
    @Expose
    val nick_nm: String? = null

    @SerializedName("stt_msg")
    @Expose
    val stt_msg: String? = null

    @SerializedName("profile_pic_path")
    @Expose
    val profile_pic_path: String? = null

    @SerializedName("nation")
    @Expose
    val nation: String? = null

    @SerializedName("lang")
    @Expose
    val lang: String? = null

    @SerializedName("birth_day")
    @Expose
    val birth_day: String? = null

    @SerializedName("login_connt_accnt")
    @Expose
    val login_connt_accnt: String? = null
}