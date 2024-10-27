package com.example.hkunexus.ui.homePages.explore

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Club
import android.view.ViewGroup as ViewGroup

class ClubListAdapter(private val dataSet: Array<Club>) :
    RecyclerView.Adapter<ClubListAdapter.ViewHolder>() {

    private var goToLandingPage: (Int) -> Unit = { position: Int -> }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.club_card)
        var clubName = view.findViewById<TextView>(R.id.club_name)
        var clubDescription = view.findViewById<TextView>(R.id.club_description)
        var clubBannerImage = view.findViewById<ImageView>(R.id.club_banner_image)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.clubName.text = dataSet[position].name
        viewHolder.clubDescription.text = dataSet[position].description
        //viewHolder.clubBannerImage.setImageDrawable(idk)

        viewHolder.cardView.setOnClickListener {
            goToLandingPage(position)
        }
    }

    override fun getItemCount() = dataSet.size

    fun setLandingCallback(callback: (Int) -> Unit) {
        goToLandingPage = callback
    }
}
