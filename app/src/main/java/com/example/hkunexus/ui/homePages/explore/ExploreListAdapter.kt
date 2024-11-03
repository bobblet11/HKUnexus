package com.example.hkunexus.ui.homePages.explore

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.dto.ClubDto
import android.view.ViewGroup as ViewGroup

class ExploreListAdapter(private val dataSet: ArrayList<ClubDto>) :
    RecyclerView.Adapter<ClubInExploreListViewHolder>() {

    private var goToLandingPage: (String) -> Unit = {
        clubId: String -> Log.d("exploreListAdapter", clubId)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ClubInExploreListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_card, viewGroup, false)
        return ClubInExploreListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ClubInExploreListViewHolder, position: Int) {
        viewHolder.clubName.text = dataSet[position].clubName
        viewHolder.clubDescription.text = dataSet[position].clubDesc
        //viewHolder.clubBannerImage.setImageDrawable(idk)
        Log.d("exploreListAdapter", position.toString())
        Log.d("exploreListAdapter", dataSet[position].toString())
        Log.d("exploreListAdapter", dataSet[position].clubId.toString())
        viewHolder.cardView.setOnClickListener {
            goToLandingPage(dataSet[position].clubId.toString())
        }
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData:  ArrayList<ClubDto>) {
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    fun setLandingCallback(callback: (String) -> Unit) { goToLandingPage = callback }
}
