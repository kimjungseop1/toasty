package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseCartridgeListDto {
    @SerializedName("root")
    @Expose
    val root: ArrayList<ROOT>? = null

    class ROOT {
        @SerializedName("ctge_no")
        @Expose
        var ctge_no: String? = null

        @SerializedName("ctge_nm")
        @Expose
        var ctge_nm: String? = null

        @SerializedName("ctge_model")
        @Expose
        var ctge_model: String? = null

        @SerializedName("ctge_model_abbr")
        @Expose
        var ctge_model_abbr: String? = null

        @SerializedName("ctge_stndrd")
        @Expose
        var ctge_stndrd: String? = null

        @SerializedName("ctge_desc")
        @Expose
        var ctge_desc: String? = null

        @SerializedName("version_no")
        @Expose
        var version_no: Int? = null

        @SerializedName("density")
        @Expose
        var density: Int? = null

        @SerializedName("back_feed_value")
        @Expose
        var back_feed_value: Int? = null

        @SerializedName("printing_process")
        @Expose
        var printing_process: Int? = null

        @SerializedName("feed_value")
        @Expose
        var feed_value: Int? = null

        @SerializedName("gap_back_feed")
        @Expose
        var gap_back_feed: Int? = null

        @SerializedName("ctge_back_url")
        @Expose
        var ctge_back_url: String? = null

        @SerializedName("device_os")
        @Expose
        var device_os: String? = null

        @SerializedName("height_ratio")
        @Expose
        var height_ratio: Double? = null

        @SerializedName("height_correction")
        @Expose
        var height_correction: Float? = null

        @SerializedName("print_area_width_ratio")
        @Expose
        var print_area_width_ratio: Float? = null

        @SerializedName("print_area_width_correction")
        @Expose
        var print_area_width_correction: Float? = null

        @SerializedName("print_area_height_ratio")
        @Expose
        var print_area_height_ratio: Float? = null

        @SerializedName("print_area_height_correction")
        @Expose
        var print_area_height_correction: Float? = null

        @SerializedName("ctge_alarm_url")
        @Expose
        var ctge_alarm_url: String? = null

        @SerializedName("character_brand")
        @Expose
        var character_brand: String? = null

        @SerializedName("ctge_icon_url")
        @Expose
        var ctge_icon_url: String? = null

        @SerializedName("ctge_tot_length")
        @Expose
        var ctge_tot_length: Float? = null

        @SerializedName("click_link")
        @Expose
        var click_link: String? = null

        @SerializedName("water_se")
        @Expose
        var water_se: String? = null
    }
}