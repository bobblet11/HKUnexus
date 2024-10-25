package com.example.hkunexus.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(

    @SerialName("id")
    val tagId: String,

    @SerialName("name")
    val tagName: String,

)
