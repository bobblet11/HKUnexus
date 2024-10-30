package com.example.hkunexus.ui.homePages.clubLanding

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R

class PostInClubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var postersUsername = view.findViewById<TextView>(R.id.postersUsername)
    var compactDescription= view.findViewById<TextView>(R.id.compactEventpostDescription)
    var timeSincePosted = view.findViewById<TextView>(R.id.timeSincePosted)
    val cardView: CardView = view.findViewById(R.id.compactEventPostCard)
}