package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestSubscribeRegDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("sub_user_id")
    @Expose
    var sub_user_id: String = ""
}