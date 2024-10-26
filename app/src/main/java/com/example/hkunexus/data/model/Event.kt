package com.example.hkunexus.data.model

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.hkunexus.R

//images should be other type, figure out later
data class Event (
    var postersUsername : String,
    var postersProfileImage : String,
    var postImage : String,
    var postText : String,
    var timeSincePosted: String,
)
