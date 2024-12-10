package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestFavoriteDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("cntnts_no")
    @Expose
    var cntnts_no: String = ""

    @SerializedName("favorite_se")
    @Expose
    var favorite_se: Int = -1
}