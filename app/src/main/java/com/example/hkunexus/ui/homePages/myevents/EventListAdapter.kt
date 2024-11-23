package com.example.hkunexus.ui.homePages.myevents

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hkunexus.BuildConfig
import com.example.hkunexus.R
import com.example.hkunexus.data.EventInterface
import com.example.hkunexus.data.model.dto.EventDto
import com.example.hkunexus.ui.JoinableEventPostViewHolder
import com.ortiz.touchview.TouchImageView
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
        var eventMap: TouchImageView = view.findViewById<TouchImageView>(R.id.map_view)
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
        val eventLocation = event.location

        viewHolder.eventName.text = event.title
        viewHolder.eventDate.text = "Date:  $localDate"
        viewHolder.eventTime.text = "Time:  $localTime"
        viewHolder.eventLocation.text = "Location:  $eventLocation"

        // Google Maps static image URL
        val centerLatLng = eventLocation
        val zoom = 11
        val size = "1080x1080"
        val mapType = "roadmap"
        val marker1 = "color:red|label:1|$centerLatLng"
        val apiKey = BuildConfig.API_KEY // Replace with your actual API key
        val url = "https://maps.googleapis.com/maps/api/staticmap?center=$centerLatLng&zoom=$zoom&size=$size&maptype=$mapType&markers=$marker1&key=$apiKey"

        Log.d("EventListAdapter", "Loading map from URL: $url")

        // Load and cache the bitmap using Glide
        Glide.with(viewHolder.eventMap.context)
            .asBitmap()
            .load(url)
            .error(R.drawable.placeholder_view_vector) // Placeholder in case of error
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    viewHolder.eventMap.setImageBitmap(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    viewHolder.eventMap.setImageDrawable(errorDrawable)
                }
            })

        EventInterface.attachListenersAndUpdatersToEventJoiningButtons(
            viewHolder.eventButtonsView,
            event.id
        )

        setAnimation(viewHolder.itemView, position)
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