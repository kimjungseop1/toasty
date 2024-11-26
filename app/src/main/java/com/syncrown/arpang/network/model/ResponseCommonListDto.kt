package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCommonListDto {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("code_nm")
    @Expose
    var code_nm: String? = null
}