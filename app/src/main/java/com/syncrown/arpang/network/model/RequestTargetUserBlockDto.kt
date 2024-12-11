package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestTargetUserBlockDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("block_user_id")
    @Expose
    var block_user_id: String = ""

    @SerializedName("block_se")
    @Expose
    var block_se: Int = -1
}