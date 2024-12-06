package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestSubscribeUserContentListDto {
    @SerializedName("sub_user_id")
    @Expose
    var sub_user_id: String = ""

    @SerializedName("currPage")
    @Expose
    var currPage: Int = -1

    @SerializedName("pageSize")
    @Expose
    var pageSize: Int = -1

    @SerializedName("hash_tag")
    @Expose
    var hash_tag: String? = null

    @SerializedName("menu_code")
    @Expose
    var menu_code: String? = null
}