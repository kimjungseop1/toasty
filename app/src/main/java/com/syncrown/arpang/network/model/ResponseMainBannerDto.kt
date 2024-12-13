package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseMainBannerDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("bbsid")
        @Expose
        var bbsid: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("file_path")
        @Expose
        var file_path: String? = null

        @SerializedName("start_dt")
        @Expose
        var start_dt: String? = null

        @SerializedName("end_dt")
        @Expose
        var end_dt: String? = null

        @SerializedName("click_link")
        @Expose
        var click_link: String? = null

        @SerializedName("disp_no")
        @Expose
        var disp_no: String? = null
    }
}