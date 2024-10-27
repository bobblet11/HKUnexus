package com.example.hkunexus.data.model

import com.example.hkunexus.ui.homePages.create.CreateFragment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(

    @SerialName("id")
    val userId : String,

    @SerialName("email_")
    val email : String,

    @SerialName("display_name_")
    val displayName : String,

    @SerialName("first_name_")
    val firstName : String,

    @SerialName("last_name_")
    val lastName : String,

    @SerialName("profile_picture")
    val profilePicture : String,

    @SerialName("banner_picture")
    val bannerPicture : String,

    @SerialName("gender")
    val gender : String,

    @SerialName("birthday")
    val birthday : String,

    @SerialName("origin")
    val origin : String,

    @SerialName("cohort_year")
    val cohortYear : Int,

    @SerialName("graduation_year")
    val graduationYear : Int,

    @SerialName("curriculum")
    val curriculum : Int,

    @SerialName("major_minor")
    val majorMinor : String,

    @SerialName("bio")
    val bio : String,

    @SerialName("joined_at")
    val joinedAt : String,

    @SerialName("created_at")
    val createdAt : String,

    @SerialName("updated_at")
    val updatedAt : String,


)
