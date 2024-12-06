package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestPublicContentSettingDto {
    @SerializedName("cntnts_no")
    @Expose
    var cntnts_no: String = ""

    @SerializedName("share_se")
    @Expose
    var share_se: String = ""

    @SerializedName("menu_code")
    @Expose
    var menu_code: String = ""

    @SerializedName("user_id")
    @Expose
    var user_id: String = ""
}