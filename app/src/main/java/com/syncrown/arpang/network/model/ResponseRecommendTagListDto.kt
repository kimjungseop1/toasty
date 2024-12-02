package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseRecommendTagListDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("seq_no")
        @Expose
        var seq_no: String? = null

        @SerializedName("order_by")
        @Expose
        var order_by: Int? = null

        @SerializedName("tag_nm")
        @Expose
        var tag_nm: String? = null
    }
}