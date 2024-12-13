package com.syncrown.arpang.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseSearchingMatchDto {
    @SerializedName("root")
    @Expose
    var root: ArrayList<Root> = ArrayList()

    class Root {
        @SerializedName("nick_nm")
        @Expose
        var nick_nm: String? = null
    }

    @SerializedName("sub1")
    @Expose
    var sub1: ArrayList<Sub1> = ArrayList()

    class Sub1 {
        @SerializedName("hashtag_nm")
        @Expose
        var hashtag_nm: String? = null
    }

    @SerializedName("sub2")
    @Expose
    var sub2: ArrayList<Sub2> = ArrayList()

    class Sub2 {
        @SerializedName("hashtag_nm")
        @Expose
        var hashtag_nm: String? = null
    }
}