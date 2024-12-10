package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestScrapUpdateDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("cntnts_no")
    @Expose
    var cntnts_no: String = ""

    @SerializedName("scrap_se")
    @Expose
    var scrap_se: Int = -1
}