package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseBlockUserListDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("block_user_id")
        @Expose
        var block_user_id: String? = null

        @SerializedName("block_nick_nm")
        @Expose
        var block_nick_nm: String? = null

        @SerializedName("block_profile_pic_path")
        @Expose
        var block_profile_pic_path: String? = null

        @SerializedName("block_user_share_cnt")
        @Expose
        var block_user_share_cnt: Int? = null
    }
}