package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestTemplateListDto {
    @SerializedName("ctgy_se")
    @Expose
    var ctgy_se: String = ""

    @SerializedName("ctgy_code_step1")
    @Expose
    var ctgy_code_step1: Int? = null

    @SerializedName("version_no_start")
    @Expose
    var version_no_start: Int? = null

    @SerializedName("version_no_end")
    @Expose
    var version_no_end: Int? = null

    @SerializedName("character_brand")
    @Expose
    var character_brand: String? = null

    @SerializedName("currPage")
    @Expose
    var currPage: Int? = null

    @SerializedName("pageSize")
    @Expose
    var pageSize: Int? = null
}