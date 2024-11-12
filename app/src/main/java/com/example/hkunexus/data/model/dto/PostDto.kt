package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(

    @SerialName("id")
    val id : String,

    @SerialName("user_id")
    val userId : String,

    @SerialName("club_id")
    val clubId : String,

    @SerialName("title")
    val title : String,

    @SerialName("body")
    val body : String,

    @SerialName("media")
    val media : String,

    @SerialName("created_at")
    var createdAt : String,

    @SerialName("event_id")
    val eventId : String? = null,

    @SerialName("event_title")
    val eventTitle : String? = null,

    @SerialName("event_body")
    val eventBody : String? = null,

    @SerialName("event_time_start")
    var eventTimeStart : String? = null,

    @SerialName("event_duration")
    val eventDuration : Int? = null,

    @SerialName("event_location")
    val eventLocation : String? = null,

    @SerialName("event_created_at")
    val eventCreatedAt: String? = null,
    )