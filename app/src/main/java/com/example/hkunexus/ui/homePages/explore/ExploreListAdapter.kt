package com.example.hkunexus.ui.homePages.explore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Club
import android.view.ViewGroup as ViewGroup

class ExploreListAdapter(private val dataset: Array<Club>) :
    RecyclerView.Adapter<ClubInExploreListViewModel>() {

    private var goToLandingPage: (Int) -> Unit = { position: Int -> }
    private var filteredClubList: Array<Club> = dataset.clone()
    private var keyword: String = ""
    private var selectedTag: String? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ClubInExploreListViewModel {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_card, viewGroup, false)
        return ClubInExploreListViewModel(view)
    }

    override fun onBindViewHolder(viewHolder: ClubInExploreListViewModel, position: Int) {
        viewHolder.clubName.text = filteredClubList[position].name
        viewHolder.clubDescription.text = filteredClubList[position].description
        //viewHolder.clubBannerImage.setImageDrawable(idk)

        viewHolder.cardView.setOnClickListener {
            goToLandingPage(position)
        }
    }

    override fun getItemCount() = filteredClubList.size

    fun setFilterKeyword(keyword: String?) {
        if (keyword == null) {
            this.keyword = ""
        } else {
            this.keyword = keyword
        }
        updateFilteredList()
    }

    fun setSelectedTag(tag: String?) {
        selectedTag = tag
        updateFilteredList()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFilteredList() {
        val trimmedKw = keyword.trim()
        if (trimmedKw == "" && selectedTag == null) {
            filteredClubList = dataset.clone()
        } else {
            val newClubList = mutableListOf<Club>()
            for (item in dataset) {
                if (selectedTag == null || item.tags.contains(selectedTag)) {
                    if (item.name.contains(trimmedKw) || item.description.contains(trimmedKw)) {
                        newClubList.add(item)
                    }
                }
            }
            filteredClubList = newClubList.toTypedArray()
        }

        notifyDataSetChanged()
    }

    fun setLandingCallback(callback: (Int) -> Unit) { goToLandingPage = callback }
}
