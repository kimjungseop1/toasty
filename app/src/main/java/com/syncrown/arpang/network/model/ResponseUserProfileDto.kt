package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserProfileDto {
    @SerializedName("msgCode")
    @Expose
    var msgCode: String = ""

    @SerializedName("user_id")
    @Expose
    var user_id: String? = null

    @SerializedName("nick_nm")
    @Expose
    var nick_nm: String? = null

    @SerializedName("mobl_no")
    @Expose
    var mobl_no: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("profile_pic_path")
    @Expose
    var profile_pic_path: String? = null

    @SerializedName("nation")
    @Expose
    var nation: String? = null

    @SerializedName("stt_msg")
    @Expose
    var stt_msg: String? = null

    @SerializedName("lang")
    @Expose
    var lang: String? = null

    @SerializedName("birth_day")
    @Expose
    var birth_day: String? = null

    @SerializedName("last_login_ds")
    @Expose
    var last_login_ds: String? = null

    @SerializedName("markt_recptn_agre")
    @Expose
    var markt_recptn_agre: String? = null
}