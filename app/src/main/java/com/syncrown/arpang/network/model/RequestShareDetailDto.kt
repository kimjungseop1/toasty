package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestShareDetailDto {
    @SerializedName("cntnts_no")
    @Expose
    var cntnts_no: String = ""
}