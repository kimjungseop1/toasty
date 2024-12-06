package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSubscribeListDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("sub_user_id")
        @Expose
        var sub_user_id: String? = null

        @SerializedName("sub_nick_nm")
        @Expose
        var sub_nick_nm: String? = null

        @SerializedName("sub_profile_pic_path")
        @Expose
        var sub_profile_pic_path: String? = null

        @SerializedName("sub_user_share_cnt")
        @Expose
        var sub_user_share_cnt: Int? = null

        @SerializedName("is_sub_me")
        @Expose
        var is_sub_me: Int? = null
    }
}