package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestIgnoreTagCheckDto {
    @SerializedName("share_hash_tag")
    @Expose
    var share_hash_tag: String = ""
}