package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTemplateListDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root>? = ArrayList()

    class Root {
        @SerializedName("seq_no")
        @Expose
        var seq_no: String? = null

        @SerializedName("ctgy_se")
        @Expose
        var ctgy_se: String? = null

        @SerializedName("ctgy_code_step1")
        @Expose
        var ctgy_code_step1: String? = null

        @SerializedName("ctgy_code_step2")
        @Expose
        var ctgy_code_step2: String? = null

        @SerializedName("ctgy_code_step1_nm")
        @Expose
        var ctgy_code_step1_nm: String? = null

        @SerializedName("ctgy_code_step2_nm")
        @Expose
        var ctgy_code_step2_nm: String? = null

        @SerializedName("templete_nm")
        @Expose
        var templete_nm: String? = null

        @SerializedName("keywrd")
        @Expose
        var keywrd: String? = null

        @SerializedName("file_origin_nm")
        @Expose
        var file_origin_nm: String? = null

        @SerializedName("file_svr_nm")
        @Expose
        var file_svr_nm: String? = null

        @SerializedName("file_path")
        @Expose
        var file_path: String? = null

        @SerializedName("file_thumb_path")
        @Expose
        var file_thumb_path: String? = null

        @SerializedName("file_size")
        @Expose
        var file_size: Int? = null

        @SerializedName("etc1")
        @Expose
        var etc1: String? = null

        @SerializedName("etc2")
        @Expose
        var etc2: String? = null

        @SerializedName("use_se")
        @Expose
        var use_se: Int? = null

        @SerializedName("dithering_se")
        @Expose
        var dithering_se: Int? = null
    }
}