package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestSubscribeUpdateDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("sub_user_id")
    @Expose
    var sub_user_id: String = ""

    @SerializedName("subscription_se")
    @Expose
    var subscription_se: Int = -1
}