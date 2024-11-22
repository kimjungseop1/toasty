package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestWithdrawalDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("break_reson")
    @Expose
    var break_reson: String? = null

    @SerializedName("etc_reson")
    @Expose
    var etc_reson: String? = null
}