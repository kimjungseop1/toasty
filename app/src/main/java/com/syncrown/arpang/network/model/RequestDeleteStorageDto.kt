package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestDeleteStorageDto {
    @SerializedName("cntnts_no")
    @Expose
    var cntnts_no: String = ""

    @SerializedName("user_id")
    @Expose
    var user_id: String = ""
}