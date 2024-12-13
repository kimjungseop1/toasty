package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseResultMatchDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("nick_nm")
        @Expose
        var nick_nm: String? = null

        @SerializedName("contents_cnt")
        @Expose
        var contents_cnt: Int? = null

        @SerializedName("user_tot_cnt")
        @Expose
        var user_tot_cnt: Int? = null
    }

    @SerializedName("sub1")
    @Expose
    var sub1: ArrayList<Sub1> = ArrayList()

    class Sub1 {
        @SerializedName("cntnts_no")
        @Expose
        var cntnts_no: String? = null

        @SerializedName("img_url")
        @Expose
        var img_url: String? = null

        @SerializedName("save_ds")
        @Expose
        var save_ds: String? = null

        @SerializedName("contents_tot_cnt")
        @Expose
        var contents_tot_cnt: Int? = null
    }

    @SerializedName("sub2")
    @Expose
    var sub2: ArrayList<Sub2> = ArrayList()

    class Sub2 {
        @SerializedName("cntnts_no")
        @Expose
        var cntnts_no: String? = null

        @SerializedName("img_url")
        @Expose
        var img_url: String? = null

        @SerializedName("save_ds")
        @Expose
        var save_ds: String? = null

        @SerializedName("contents_tot_cnt")
        @Expose
        var contents_tot_cnt: Int? = null
    }
}