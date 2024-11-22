package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseAppMainDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root>? = null

    class Root {
        @SerializedName("code")
        @Expose
        var code: String? = null

        @SerializedName("code_nm")
        @Expose
        var code_nm: String? = null

        @SerializedName("order_by")
        @Expose
        var order_by: Int? = null

        @SerializedName("app_id")
        @Expose
        var app_id: String? = null
    }
}