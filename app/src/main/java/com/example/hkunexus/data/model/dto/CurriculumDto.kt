package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurriculumDto(

    @SerialName("id")
    val id : Int,

    @SerialName("curriculum")
    val title : String,

)
