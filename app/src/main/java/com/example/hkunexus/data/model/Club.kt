package com.example.hkunexus.data.model

import com.example.hkunexus.data.model.dto.ClubDto

data class Club (
    var data : ClubDto,
    var joined: Boolean = false,
    val tags: Array<String> = arrayOf(),
    var numberOfMembers: Int = 0,
)