package com.example.hkunexus.ui.homePages.explore

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R

class ClubInExploreListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val clubImageContainer: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.club_banner_container)
    val cardView: CardView = view.findViewById(R.id.club_card)
    val clubName = view.findViewById<TextView>(R.id.club_name)
    val clubDescription = view.findViewById<TextView>(R.id.club_description)
    val clubBannerImage = view.findViewById<ImageView>(R.id.club_banner_image)
}