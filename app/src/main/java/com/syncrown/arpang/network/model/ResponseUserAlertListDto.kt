package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserAlertListDto {
    @SerializedName("root")
    @Expose
    var root: Root? = null

    class Root {
        @SerializedName("noti_event_se")
        @Expose
        var noti_event_se: Int? = null

        @SerializedName("subscrip_se")
        @Expose
        var subscrip_se: Int? = null

        @SerializedName("favor_se")
        @Expose
        var favor_se: Int? = null

        @SerializedName("comment_se")
        @Expose
        var comment_se: Int? = null
    }
}