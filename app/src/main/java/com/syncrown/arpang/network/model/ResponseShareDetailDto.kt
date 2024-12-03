package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseShareDetailDto {
    @SerializedName("root")
    @Expose
    var root: ROOT? = null

    class ROOT {
        @SerializedName("cntnts_no")
        @Expose
        var cntnts_no: String? = null

        @SerializedName("img_url")
        @Expose
        var img_url: String? = null

        @SerializedName("ctge_no")
        @Expose
        var ctge_no: String? = null

        @SerializedName("pixel_x")
        @Expose
        var pixel_x: Int? = null

        @SerializedName("save_ds")
        @Expose
        var save_ds: String? = null

        @SerializedName("nick_nm")
        @Expose
        var nick_nm: String? = null

        @SerializedName("profile_pic_path")
        @Expose
        var profile_pic_path: String? = null

        @SerializedName("ctge_nm")
        @Expose
        var ctge_nm: String? = null

        @SerializedName("comment_cnt")
        @Expose
        var comment_cnt: Int? = null

        @SerializedName("favorite_cnt")
        @Expose
        var favorite_cnt: Int? = null
    }
}