package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventToPost(
    @SerialName("event_id")
    val eventId : String,
    @SerialName("post_id")
    val postId : String,
)