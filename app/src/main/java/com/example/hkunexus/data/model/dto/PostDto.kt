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
    val createdAt : String,

    val isEvent : Boolean = true,

)