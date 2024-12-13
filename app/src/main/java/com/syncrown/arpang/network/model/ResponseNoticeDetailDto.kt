package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseNoticeDetailDto {
    @SerializedName("root")
    @Expose
    var root : Root? = null

    class Root {
        @SerializedName("bbsid")
        @Expose
        var bbsid: String? = null

        @SerializedName("read_se")
        @Expose
        var read_se: String? = null

        @SerializedName("lang_code")
        @Expose
        var lang_code: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("content")
        @Expose
        var content: String? = null

        @SerializedName("start_dt")
        @Expose
        var start_dt: String? = null

        @SerializedName("end_dt")
        @Expose
        var end_dt: String? = null

        @SerializedName("app_id")
        @Expose
        var app_id: String? = null
    }
}