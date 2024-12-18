package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLibPaperListDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("ctge_no")
        @Expose
        var ctge_no: String? = null

        @SerializedName("ctge_nm")
        @Expose
        var ctge_nm: String? = null
    }
}