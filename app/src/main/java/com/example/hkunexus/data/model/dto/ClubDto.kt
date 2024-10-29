package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubDto(

    @SerialName("id")
    val clubId: String?,

    @SerialName("created_at")
    val createdAt: String?,

    @SerialName("name")
    val clubName: String?,

    @SerialName("description")
    val clubDesc: String?,

)




