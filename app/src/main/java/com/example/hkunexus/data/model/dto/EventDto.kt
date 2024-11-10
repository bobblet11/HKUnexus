package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(

    @SerialName("id")
    var id : String?,

    @SerialName("club_id")
    var clubId : String?,

    @SerialName("title")
    var title : String?,

    @SerialName("body")
    var body : String?,

    @SerialName("time_start")
    var timeStart : String?,

    @SerialName("duration")
    var duration : Int?,

    @SerialName("location")
    var location : String?,

    @SerialName("created_at")
    var createdAt: String?,

) : java.io.Serializable

public fun fromPostToEvent(post: PostDto) : EventDto{
    val e = EventDto(id=post.eventId, clubId = post.clubId, title = post.eventTitle, body = post.eventBody, timeStart = post.eventTimeStart, duration = post.eventDuration, location = post.eventLocation, createdAt = post.eventCreatedAt)
    return e
}
