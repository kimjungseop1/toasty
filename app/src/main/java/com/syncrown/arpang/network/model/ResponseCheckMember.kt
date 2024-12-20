package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCheckMember {
    @SerializedName("msgCode")
    @Expose
    var msgCode: String? = null

    @SerializedName("member_check")
    @Expose
    var member_check: Int = -1
}