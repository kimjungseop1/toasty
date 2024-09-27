package com.syncrown.toasty.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestJoinDto {
    @SerializedName("user_id")
    @Expose
    val user_id: String = ""

    @SerializedName("login_connt_accnt")
    @Expose
    val login_connt_accnt: String = ""

    @SerializedName("userid_se")
    @Expose
    val userid_se: String = ""

    @SerializedName("user_nm")
    @Expose
    val user_nm: String? = null

    @SerializedName("use_se")
    @Expose
    val use_se: Int = -1

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

    @SerializedName("nation")
    @Expose
    val nation: String? = null

    @SerializedName("lang")
    @Expose
    val lang: String? = null

    @SerializedName("birth_day")
    @Expose
    val birth_day: String? = null

    @SerializedName("markt_recptn_agre")
    @Expose
    val markt_recptn_agre: Int? = null

    @SerializedName("profile")
    @Expose
    val profile: String? = null
}