package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(

    @SerialName("id")
    val id : String,

    @SerialName("email_")
    val email : String,

    @SerialName("display_name_")
    val displayName : String,

    @SerialName("first_name_")
    val firstName : String,

    @SerialName("last_name_")
    val lastName : String,

    @SerialName("profile_picture")
    val profilePicture : String,

    )
