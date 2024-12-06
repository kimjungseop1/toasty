package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSubscribeTotalDto {
    @SerializedName("root")
    @Expose
    var root: Root? = null

    class Root {
        @SerializedName("my_subscription_cnt")
        @Expose
        var my_subscription_cnt: Int? = null

        @SerializedName("me_subscription_cnt")
        @Expose
        var me_subscription_cnt: Int? = null

        @SerializedName("msgCode")
        @Expose
        var msgCode: String? = null
    }
}