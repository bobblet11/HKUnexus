package com.example.hkunexus.ui.homePages.clubLanding

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R

public class PostInClubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cardView: CardView = view.findViewById(R.id.compactPostCard)

    var postersUsername = view.findViewById<TextView>(R.id.postersUsername)
    var description = view.findViewById<TextView>(R.id.postDescription)
    var timeSincePosted = view.findViewById<TextView>(R.id.timeSincePosted)
}
