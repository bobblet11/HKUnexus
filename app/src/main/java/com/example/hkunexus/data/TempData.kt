package com.example.hkunexus.data

import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.Event
import com.example.hkunexus.data.model.GenericPost
import com.example.hkunexus.data.model.Post
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

    val clubPosts: Array<Post> = arrayOf(
        Event(
            postTitle = "Community Cleanup Event",
            posterUsername = "john_doe",
            postersProfileImage = "https://example.com/images/john.jpg",
            postImage = "https://example.com/images/event1.jpg",
            postText = "Excited to announce our upcoming community cleanup event!",
            timeSincePosted = "4 days ago"
        ),
        GenericPost(
            postTitle = "Hiking",
            posterUsername = "jane_smith",
            postersProfileImage = "https://example.com/images/jane.jpg",
            postImage = "https://example.com/images/event2.jpg",
            postText = "Join us for a fun day of hiking this Saturday!",
            timeSincePosted = "9 days ago"
        ),
        GenericPost(
            postTitle = "Charity Bake Sale",
            posterUsername = "mark_taylor",
            postersProfileImage = "https://example.com/images/mark.jpg",
            postImage = "https://example.com/images/event3.jpg",
            postText = "Don't miss our charity bake sale next week!",
            timeSincePosted = "17 days ago"
        ),
        Event(
            postTitle = "Movie Night",
            posterUsername = "lisa_white",
            postersProfileImage = "https://example.com/images/lisa.jpg",
            postImage = "https://example.com/images/event4.jpg",
            postText = "We're hosting a movie night under the stars! Bring your blankets!",
            timeSincePosted = "19 days ago"
        )
    )

    val events: Array<Post> = arrayOf(
        Event(
            postTitle = "Community Cleanup Event",
            posterUsername = "john_doe",
            postersProfileImage = "https://example.com/images/john.jpg",
            postImage = "https://example.com/images/event1.jpg",
            postText = "Excited to announce our upcoming community cleanup event!",
            timeSincePosted = "4 days ago"
        ),
        Event(
            postTitle = "Hiking",
            posterUsername = "jane_smith",
            postersProfileImage = "https://example.com/images/jane.jpg",
            postImage = "https://example.com/images/event2.jpg",
            postText = "Join us for a fun day of hiking this Saturday!",
            timeSincePosted = "9 days ago"
        ),
        Event(
            postTitle = "Charity Bake Sale",
            posterUsername = "mark_taylor",
            postersProfileImage = "https://example.com/images/mark.jpg",
            postImage = "https://example.com/images/event3.jpg",
            postText = "Don't miss our charity bake sale next week!",
            timeSincePosted = "17 days ago"
        ),
        Event(
            postTitle = "Movie Night",
            posterUsername = "lisa_white",
            postersProfileImage = "https://example.com/images/lisa.jpg",
            postImage = "https://example.com/images/event4.jpg",
            postText = "We're hosting a movie night under the stars! Bring your blankets!",
            timeSincePosted = "19 days ago"
        )
    )


}