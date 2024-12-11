package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestTargetUserReportDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("complain_user_id")
    @Expose
    var complain_user_id: String = ""

    @SerializedName("complain_desc")
    @Expose
    var complain_desc: String? = null
}