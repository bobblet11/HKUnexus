package com.example.hkunexus.data.model

//images should be other type, figure out later
data class GenericPost (
    override var postTitle: String,
    override var postText : String,
    override var posterUsername : String,
    override var timeSincePosted: String,
    var postersProfileImage : String,
    var postImage : String,
) : Post
