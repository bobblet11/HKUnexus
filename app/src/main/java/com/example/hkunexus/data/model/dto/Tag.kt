package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(

    @SerialName("id")
    val id: String?,

    @SerialName("name")
    val tagName: String,

    )
