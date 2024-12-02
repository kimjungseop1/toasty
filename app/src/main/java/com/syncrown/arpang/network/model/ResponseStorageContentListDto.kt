package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseStorageContentListDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<ROOT>? = null

    class ROOT {
        @SerializedName("cntnts_no")
        @Expose
        var cntnts_no: Int? = null

        @SerializedName("img_url")
        @Expose
        var img_url: String? = null

        @SerializedName("ctge_no")
        @Expose
        var ctge_no: Int? = null

        @SerializedName("pixel_x")
        @Expose
        var pixel_x: Double? = null

        @SerializedName("save_ds")
        @Expose
        var save_ds: String? = null

        @SerializedName("comment_cnt")
        @Expose
        var commnet_cnt: Int? = null

        @SerializedName("favorite_cnt")
        @Expose
        var favorite_cnt: Int? = null
    }
}