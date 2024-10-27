package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubToTagDto(
    @SerialName("club_id")
    val clubId : String,
    @SerialName("tag_id")
    val tagId : String,
)
