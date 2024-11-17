package com.example.hkunexus.ui.homePages.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R

class PostInHomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cardView: CardView = view.findViewById(R.id.compactPostCard)
    var clubName = view.findViewById<TextView>(R.id.clubNamePost)
    var postTitle = view.findViewById<TextView>(R.id.postTitle)
    var postersUsername = view.findViewById<TextView>(R.id.postersUsername)
    var description = view.findViewById<TextView>(R.id.postDescription)
    var timeSincePosted = view.findViewById<TextView>(R.id.timeSincePosted)
    var postImage = view.findViewById<ImageView>(R.id.postImage)
}
