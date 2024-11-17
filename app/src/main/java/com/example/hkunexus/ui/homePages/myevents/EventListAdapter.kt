package com.example.hkunexus.ui.homePages.myevents

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.EventInterface
import com.example.hkunexus.data.model.dto.EventDto
import com.example.hkunexus.ui.JoinableEventPostViewHolder
import java.time.ZoneId
import java.time.ZonedDateTime
import android.view.ViewGroup as ViewGroup

class EventListAdapter(private val dataSet: ArrayList<EventDto>, private val context: Context) :
    RecyclerView.Adapter<EventListAdapter.ViewHolder>() {
    private var lastPosition = -1
    class ViewHolder(view: View) : JoinableEventPostViewHolder(view) {
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

        EventInterface.attachListenersAndUpdatersToEventJoiningButtons(
            viewHolder.eventButtonsView,
            event.id
        )
        setAnimation(viewHolder.itemView, position);
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, R.anim.fade)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData:  ArrayList<EventDto>) {
        // Call when the data changes.
        Log.d("CLEARING","CLEARING")
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
    }
}