package com.example.hkunexus.ui.homePages.create.ui.createselectclub

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.dto.ClubDto

class ClubListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.club_name)
    val cardView: CardView = view.findViewById(R.id.club_card)
}

class ClubListAdapter(private val dataSet: ArrayList<ClubDto>) :
    RecyclerView.Adapter<ClubListViewHolder>() {

    private var select: (ClubDto) -> Unit = {
        _: ClubDto ->
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ClubListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.create_select_clubs_card, viewGroup, false)

        return ClubListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ClubListViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position].clubName
        Log.d("clubListAdapter", position.toString())
        Log.d("clubListAdapter", dataSet[position].toString())
        Log.d("clubListAdapter", dataSet[position].clubId.toString())
        viewHolder.cardView.setOnClickListener {
            select(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    fun setSelectCallback(callback: (ClubDto) -> Unit) { select = callback }


    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData:  ArrayList<ClubDto>) {
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
        Log.d("adapter", dataSet.toString())
    }

}
