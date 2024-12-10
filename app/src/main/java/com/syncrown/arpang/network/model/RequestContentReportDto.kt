package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestContentReportDto {
    @SerializedName("cntnts_no")
    @Expose
    var cntnts_no: String = ""

    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("write_user_id")
    @Expose
    var write_user_id: String = ""

    @SerializedName("complain_desc")
    @Expose
    var complain_desc: String? = null
}