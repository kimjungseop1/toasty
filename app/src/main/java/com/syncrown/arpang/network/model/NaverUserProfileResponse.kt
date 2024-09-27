package com.syncrown.arpang.network.model

import com.google.gson.annotations.SerializedName

data class NaverUserProfileResponse(
    @SerializedName("response")
    val response: NaverUserResponse
)

data class NaverUserResponse(
    val email: String,
    val name: String
)
