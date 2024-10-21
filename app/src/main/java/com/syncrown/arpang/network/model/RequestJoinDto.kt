package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestJoinDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("login_connt_accnt")
    @Expose
    var login_connt_accnt: String = ""

    @SerializedName("userid_se")
    @Expose
    var userid_se: String = ""

    @SerializedName("user_nm")
    @Expose
    var user_nm: String? = null

    @SerializedName("use_se")
    @Expose
    var use_se: Int = -1

    @SerializedName("mobl_no")
    @Expose
    var mobl_no: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("nick_nm")
    @Expose
    var nick_nm: String? = null

    @SerializedName("stt_msg")
    @Expose
    var stt_msg: String? = null

    @SerializedName("nation")
    @Expose
    var nation: String? = null

    @SerializedName("lang")
    @Expose
    var lang: String? = null

    @SerializedName("birth_day")
    @Expose
    var birth_day: String? = null

    @SerializedName("markt_recptn_agre")
    @Expose
    var markt_recptn_agre: Int? = null

    @SerializedName("profile")
    @Expose
    var profile: String? = null
}