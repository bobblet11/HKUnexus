package com.example.hkunexus.data.model

data class Club (
    var clubID: Int,
    var name: String,
    var description: String,
    var joined: Boolean = false,
    var tags: Array<String> = arrayOf(),
    var numberOfMembers: Int = 0,
)