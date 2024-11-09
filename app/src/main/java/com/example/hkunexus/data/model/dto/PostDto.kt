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
    val eventId : String?,

    @SerialName("event_title")
    val eventTitle : String?,

    @SerialName("event_body")
    val eventBody : String?,

    @SerialName("event_time_start")
    val eventTimeStart : String?,

    @SerialName("event_duration")
    val eventDuration : Int?,

    @SerialName("event_location")
    val eventLocation : String?,

    @SerialName("event_created_at")
    val eventCreatedAt: String?,
    )