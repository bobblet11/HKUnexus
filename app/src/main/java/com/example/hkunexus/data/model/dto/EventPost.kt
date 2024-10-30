package com.example.hkunexus.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EventPost (
    var id: String,
    var user_id: String,
    var club_id: String,
    var title: String,
    var body: String,
    var media: String,
    var created_at: String
)
