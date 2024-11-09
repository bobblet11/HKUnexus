package com.example.hkunexus.ui.homePages.myevents

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.dto.EventDto
import java.time.ZoneId
import java.time.ZonedDateTime
import android.view.ViewGroup as ViewGroup

class EventListAdapter(private val dataSet: List<EventDto>) :
    RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    private var goToPostPage: (Int) -> Unit = { postID: Int -> }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var eventName: TextView = view.findViewById<TextView>(R.id.eventName)
        var eventDate: TextView = view.findViewById<TextView>(R.id.eventName3)
        var eventTime: TextView = view.findViewById<TextView>(R.id.eventName5)
        var eventLocation: TextView = view.findViewById<TextView>(R.id.eventName6)
        val cardView: CardView = view.findViewById(R.id.event_widget_card)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_event_widget, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val event = dataSet[position]
        val timeStart = event.timeStart // e.g., '2024-12-25 14:00:00+00'
        val zonedDateTime = ZonedDateTime.parse(timeStart)
        val clientZoneId = ZoneId.systemDefault()
        val localDateTime = zonedDateTime.withZoneSameInstant(clientZoneId)
        val localDate = localDateTime.toLocalDate().toString()
        val localTime = localDateTime.toLocalTime().toString()

        viewHolder.eventName.text = event.title
        viewHolder.eventDate.text = "Date:  $localDate" // Use the local date part
        viewHolder.eventTime.text = "Time:  $localTime" // Use the local time part
        viewHolder.eventLocation.text = "Location:  " + event.location

        viewHolder.cardView.setOnClickListener {
            goToPostPage(position)
        }
    }

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (Int) -> Unit) {
        goToPostPage = callback
    }
}