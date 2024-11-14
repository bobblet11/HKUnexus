package com.example.hkunexus.ui.homePages.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.PostDto
import com.example.hkunexus.data.EventInterface

class PostInHomeListAdapter(private val dataSet: ArrayList<PostDto>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var goToPostPage: (String) -> Unit = { postID: String -> }

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return if (dataSet[position].eventId == null) 1 else 0
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            //EVENT POST
            0 -> {
                val view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.fragment_post_event_card_home, viewGroup, false)
                return EventPostInHomeViewHolder(view)
            }
            //NORMAL POST
            1 -> {
                val view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.fragment_post_card_home, viewGroup, false)
                return PostInHomeViewHolder(view)
            }
            //DEFAULT IS NORMAL POST
            else -> {
                val view = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.fragment_post_card_home, viewGroup, false)
                return PostInHomeViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            //EVENT POST
            0 -> {
                val viewHolder: EventPostInHomeViewHolder = holder as EventPostInHomeViewHolder
//                viewHolder.postersUsername.text = SupabaseSingleton.getDisplayName(dataSet[position].userId)

                viewHolder.postersUsername.text = "posted by " + dataSet[position].displayName
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.eventLocation.text = dataSet[position].eventLocation
                viewHolder.eventTime.text = dataSet[position].eventTimeStart
                viewHolder.postTitle.text = dataSet[position].title
                viewHolder.clubName.text = dataSet[position].clubName

                val eventId = dataSet[position].eventId!!

                fun updateButtonVisibility() {
                    val joined = EventInterface.hasJoinedEvent(eventId)

                    if (joined) {
                        viewHolder.joinButton.visibility = View.GONE
                        viewHolder.leaveButton.visibility = View.VISIBLE
                    } else {
                        viewHolder.joinButton.visibility = View.VISIBLE
                        viewHolder.leaveButton.visibility = View.GONE
                    }
                }

                viewHolder.setJoinCallback {
                    EventInterface.joinEvent(eventId)
                    updateButtonVisibility()

                }

                viewHolder.setLeaveCallback {
                    EventInterface.leaveEvent(eventId)
                    updateButtonVisibility()
                }

                updateButtonVisibility()

                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }
            }
            //NORMAL POST
            1 -> {
                val viewHolder: PostInHomeViewHolder = holder as PostInHomeViewHolder
//                viewHolder.postersUsername.text =  SupabaseSingleton.getDisplayName(dataSet[position].userId)
                viewHolder.postersUsername.text = "posted by " + dataSet[position].displayName
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.postTitle.text = dataSet[position].title
                viewHolder.clubName.text = dataSet[position].clubName
                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }
            }
            //DEFAULT IS NORMAL POST
            else -> {
                val viewHolder: PostInHomeViewHolder = holder as PostInHomeViewHolder
                viewHolder.postersUsername.text =
                    "posted by " + SupabaseSingleton.getDisplayName(dataSet[position].userId)
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.postTitle.text = dataSet[position].title
                viewHolder.clubName.text = dataSet[position].clubName

                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (String) -> Unit) {
        goToPostPage = callback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newData: ArrayList<PostDto>) {
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged()
    }

}
