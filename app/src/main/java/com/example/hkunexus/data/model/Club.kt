package com.example.hkunexus.data.model

data class Club (
    var name: String,
    var description: String,
    var joined: Boolean,
    var tags: Array<String>,
    var numberOfMembers: Int = 0,
    var clubID: Int = 0
)