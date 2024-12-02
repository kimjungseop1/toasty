package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestCartridgeListByTagDto {
    @SerializedName("tag_seq_no")
    @Expose
    var tag_seq_no: String = ""

    @SerializedName("app_id")
    @Expose
    var app_id: String = ""

    @SerializedName("menu_code")
    @Expose
    var menu_code: String? = null

    @SerializedName("currPage")
    @Expose
    var currPage: Int? = null

    @SerializedName("pageSize")
    @Expose
    var pageSize: Int? = null
}