package com.example.hkunexus.ui.homePages.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.EventInterface
import com.example.hkunexus.data.model.dto.PostDto


class PostInHomeListAdapter(private val dataSet: ArrayList<PostDto>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lastPosition = -1

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

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, R.anim.fade)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
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

                val eventId = dataSet[position].eventId

                // Call this updater function if you want to update the states of buttons
                val updater = EventInterface.attachListenersAndUpdatersToEventJoiningButtons(
                    viewHolder.eventButtonsView, eventId
                )

                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }

                setAnimation(viewHolder.itemView, position);
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
                setAnimation(viewHolder.itemView, position);
            }
            //DEFAULT IS NORMAL POST
            else -> {
                val viewHolder: PostInHomeViewHolder = holder as PostInHomeViewHolder
                viewHolder.postersUsername.text = "posted by " + dataSet[position].displayName
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.postTitle.text = dataSet[position].title
                viewHolder.clubName.text = dataSet[position].clubName

                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }

                setAnimation(viewHolder.itemView, position);
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
