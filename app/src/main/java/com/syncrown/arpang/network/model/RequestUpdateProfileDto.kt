package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestUpdateProfileDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("nick_nm")
    @Expose
    var nick_nm: String? = null

    @SerializedName("mobl_no")
    @Expose
    var mobl_no: String? = null

    @SerializedName("profile")
    @Expose
    var profile: String? = null

    @SerializedName("stt_msg")
    @Expose
    var stt_msg: String? = null

    @SerializedName("lang")
    @Expose
    var lang: String? = null
}