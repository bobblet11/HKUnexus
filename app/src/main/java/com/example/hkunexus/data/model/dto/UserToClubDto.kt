package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserToClubDto(
    @SerialName("user_id")
    val userId : String,
    @SerialName("club_id")
    val clubId : String,
    @SerialName("role")
    val role : String
)
