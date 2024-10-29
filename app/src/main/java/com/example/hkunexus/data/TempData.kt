package com.example.hkunexus.data

import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.Event
import com.example.hkunexus.data.model.GenericPost
import com.example.hkunexus.data.model.Post

object TempData {

    val tags: Array<String> = arrayOf(
        "Genshin",
        "Relax",
        "Mahjong"
    )

    val clubs: Array<Club> = arrayOf(
        Club(
            "BENS Tennis club!",
            "come join me and my friends in tennis! We are all beginners!",
            false,
            arrayOf("Relax")
        ),
        Club(
            "Mahjong Mondays",
            "Do you like mahjong? Do you hate Mondays? Then why don't you make your monday better with MAHJONG!",
            false,
            arrayOf("Relax", "Mahjong")
        ),
        Club(
            "Genshin Impact learners",
            "wanna learn tips and tricks to max your character? come join our lessons!",
            false,
            arrayOf("Genshin", "Relax")
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