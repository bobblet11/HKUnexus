package com.example.hkunexus.ui.homePages.myevents

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Event
import com.example.hkunexus.data.model.Post
import android.view.ViewGroup as ViewGroup

class EventListAdapter(private val dataSet: Array<Post>) :
    RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    private var goToPostPage: (Int) -> Unit = { postID: Int -> }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var postersUsername = view.findViewById<TextView>(R.id.postersUsername)
        var compactEventDescription= view.findViewById<TextView>(R.id.compactEventDescription)
        var eventBannerImage = view.findViewById<ImageView>(R.id.eventBannerImage)
        var timeSincePosted = view.findViewById<TextView>(R.id.timeSincePosted)
        var postersProfileImage = view.findViewById<ImageView>(R.id.postersProfileImage)
        val cardView: CardView = view.findViewById(R.id.compactEventCard)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.compact_event_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.postersUsername.text = dataSet[position].posterUsername
        viewHolder.compactEventDescription.text = dataSet[position].postText
        viewHolder.timeSincePosted.text = dataSet[position].timeSincePosted

        viewHolder.cardView.setOnClickListener {
            goToPostPage(position)
        }
    }

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (Int) -> Unit) {
        goToPostPage = callback
    }
}
