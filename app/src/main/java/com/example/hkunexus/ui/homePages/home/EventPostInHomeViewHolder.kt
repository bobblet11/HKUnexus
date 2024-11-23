package com.example.hkunexus.ui.homePages.home

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.ui.JoinableEventPostViewHolder

class EventPostInHomeViewHolder(view: View) : JoinableEventPostViewHolder(view) {
    val cardView: CardView = view.findViewById(R.id.compactEventPostCard)
    var clubName = view.findViewById<TextView>(R.id.clubNamePost)
    var postersUsername = view.findViewById<TextView>(R.id.postersUsername)
    var postTitle = view.findViewById<TextView>(R.id.postTitle)
    var description = view.findViewById<TextView>(R.id.postDescription)
    var timeSincePosted = view.findViewById<TextView>(R.id.timeSincePosted)

    var postImageContainer = view.findViewById<CardView>(R.id.imageCard)
    var postImage = view.findViewById<ImageView>(R.id.postImageEvent)

    var eventLocation = view.findViewById<TextView>(R.id.eventLocation)
    var eventTime = view.findViewById<TextView>(R.id.eventTime)
    var clubPfp = view.findViewById<ImageView>(R.id.groupProfileImage)
}
