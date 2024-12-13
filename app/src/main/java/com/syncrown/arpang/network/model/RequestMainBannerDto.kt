package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestMainBannerDto {
    @SerializedName("banner_se_code")
    @Expose
    var banner_se_code: String = ""

    @SerializedName("menu_code")
    @Expose
    var menu_code: String = ""
}