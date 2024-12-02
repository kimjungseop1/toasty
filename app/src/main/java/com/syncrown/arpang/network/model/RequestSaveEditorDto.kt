package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestSaveEditorDto {
    @SerializedName("user_id")
    @Expose
    var user_id: String = ""

    @SerializedName("ctge_no")
    @Expose
    var ctge_no: Int = -1

    @SerializedName("main_image")
    @Expose
    var main_image: String = ""

    @SerializedName("editor_data")
    @Expose
    var editor_data: String? = null

    @SerializedName("pixel_x")
    @Expose
    var pixel_x: Double? = null

    @SerializedName("share_hash_tag")
    @Expose
    var share_hash_tag: String? = null

    @SerializedName("share_se")
    @Expose
    var share_se: Int = -1

    @SerializedName("menu_code")
    @Expose
    var menu_code: Int = -1

    @SerializedName("parent_cntnts_no")
    @Expose
    var parent_cntnts_no: String? = null
}