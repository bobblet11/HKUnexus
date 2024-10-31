package com.example.hkunexus.ui.homePages.explore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.dto.ClubDto
import android.view.ViewGroup as ViewGroup

class ExploreListAdapter(private val dataSet: ArrayList<ClubDto>) :
    RecyclerView.Adapter<ClubInExploreListViewHolder>() {

    private var goToLandingPage: (Int) -> Unit = { position: Int -> }
    private var filteredClubList: ArrayList<ClubDto> = ArrayList(dataSet)
    private var keyword: String = ""
    private var selectedTag: String? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ClubInExploreListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_card, viewGroup, false)
        return ClubInExploreListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ClubInExploreListViewHolder, position: Int) {
        viewHolder.clubName.text = filteredClubList[position].clubName
        viewHolder.clubDescription.text = filteredClubList[position].clubDesc
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
            filteredClubList.clear()
            filteredClubList.addAll(dataSet)
        } else {
            val filteredList = mutableListOf<ClubDto>()
            for (item in dataSet) {

                if (selectedTag != null && !item.tags.contains(selectedTag)){
                    continue
                }

                if (!item.clubName!!.contains(trimmedKw) && !item.clubDesc!!.contains(trimmedKw)) {
                    continue
                }

                filteredList.add(item)
            }
            filteredClubList = filteredList.toTypedArray().toCollection(ArrayList())
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData:  ArrayList<ClubDto>) {
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    fun setLandingCallback(callback: (Int) -> Unit) { goToLandingPage = callback }
}
