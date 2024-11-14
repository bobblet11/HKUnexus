package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserToEventDto(
    @SerialName("user_id")
    val userId : String,
    @SerialName("event_id")
    val clubId : String,
)
