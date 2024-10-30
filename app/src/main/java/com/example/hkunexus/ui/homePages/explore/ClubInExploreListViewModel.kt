package com.example.hkunexus.ui.homePages.explore

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R

class ClubInExploreListViewModel(view: View) : RecyclerView.ViewHolder(view) {
    val cardView: CardView = view.findViewById(R.id.club_card)
    var clubName = view.findViewById<TextView>(R.id.club_name)
    var clubDescription = view.findViewById<TextView>(R.id.club_description)
    var clubBannerImage = view.findViewById<ImageView>(R.id.club_banner_image)

}