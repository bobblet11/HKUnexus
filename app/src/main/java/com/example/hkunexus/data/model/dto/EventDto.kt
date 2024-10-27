package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(

    @SerialName("id")
    val id : String,

    @SerialName("club_id")
    val clubId : String,

    @SerialName("title")
    val title : String,

    @SerialName("body")
    val body : String,

    @SerialName("time_start")
    val timeStart : String,

    @SerialName("duration")
    val duration : Int,

    @SerialName("location")
    val location : String,

    @SerialName("created_at")
    val createdAt: String,
)
