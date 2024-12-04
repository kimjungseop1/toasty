package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCommentListDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("seq_no")
        @Expose
        var seq_no: String? = null

        @SerializedName("comment")
        @Expose
        var comment: String? = null

        @SerializedName("nick_nm")
        @Expose
        var nick_nm: String? = null

        @SerializedName("profile_pic_path")
        @Expose
        var profile_pic_path: String? = null

        @SerializedName("save_ds")
        @Expose
        var save_ds: String? = null

        @SerializedName("write_se")
        @Expose
        var write_se: String? = null
    }
}