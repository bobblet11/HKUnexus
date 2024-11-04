package com.example.hkunexus.data

import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.Tag

object TempData {

    val tags: Array<Tag> = arrayOf(
        Tag("a", "Genshin"),
        Tag("b", "Relax"),
        Tag("c", "Mahjong")
    )

    val clubs: Array<ClubDto> = arrayOf(
        ClubDto(
            clubId = "0",
            clubName = "BENS Tennis club!",
            clubDesc = "come join me and my friends in tennis! We are all beginners!",
        ),
        ClubDto(
            clubId = "1",
            clubName = "Mahjong Monday!",
            clubDesc = "hate mondays? Love mahjong? come join!",
        ),
        ClubDto(
            clubId = "2",
            clubName = "Genshin club",
            clubDesc = "are you a weeb? do you play genshin? then join us!",

        )
    )


}