package com.example.hkunexus.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClubDto(

    @SerialName("id")
    val clubId: String? = "0",

    @SerialName("created_at")
    val createdAt: String? = "",

    @SerialName("name")
    var clubName: String? = "default",

    @SerialName("description")
    var clubDesc: String? = "default",

    @SerialName("image")
    var clubImage: String? = "",

    var joined: Boolean = false,
    val tags: Array<String> = arrayOf(),
    var numberOfMembers: Int = 0,
) : java.io.Serializable



