package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCheckMember {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""
}