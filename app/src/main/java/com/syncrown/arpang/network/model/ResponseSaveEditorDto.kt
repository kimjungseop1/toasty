package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSaveEditorDto {
    @SerializedName("msgCode")
    @Expose
    var msgCode: String? = null
}