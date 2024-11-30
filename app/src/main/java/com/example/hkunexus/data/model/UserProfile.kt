package com.example.hkunexus.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserProfile(
    @SerialName("user_uuid")
    val uuid : String,
    @SerialName("display_name")
    val username : String,

    @SerialName("joined_at")
    val joined_at : String,

    @SerialName("bio")
    val bio : String,
)
