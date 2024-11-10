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
import com.example.hkunexus.data.model.dto.EventDto

class EventListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.event_name2)
    val cardView: CardView = view.findViewById(R.id.club_card)
    val timeStart: TextView = view.findViewById(R.id.event_start_time)
    val location: TextView = view.findViewById(R.id.event_location)
}

class EventListAdapter(private val dataSet: ArrayList<EventDto>) :
    RecyclerView.Adapter<EventListViewHolder>() {

    private var select: (EventDto) -> Unit = {
        _: EventDto ->
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EventListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.create_select_event_card, viewGroup, false)

        return EventListViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: EventListViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position].title
        viewHolder.timeStart.text = dataSet[position].timeStart
        viewHolder.location.text = dataSet[position].location

        viewHolder.cardView.setOnClickListener {
            select(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size

    fun setSelectCallback(callback: (EventDto) -> Unit) { select = callback }


    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData:  ArrayList<EventDto>) {
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
        Log.d("adapter", dataSet.toString())
    }

}
