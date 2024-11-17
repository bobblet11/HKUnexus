package com.example.hkunexus.ui.homePages.clubLanding

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.PostDto
import com.example.hkunexus.data.EventInterface

public final class PostInClubListAdapter(private val dataSet: ArrayList<PostDto>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var goToPostPage: (String) -> Unit = { postID: String -> }
    private var lastPosition = -1
    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous

        return if (dataSet[position].eventId == null) 1 else 0
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //creates the PostInCLubViewHolder i.e. a post card/event post card.
        when(viewType){
            //EVENT POST
            0 -> {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_post_event_card, viewGroup, false)
                return EventPostInClubViewHolder(view)
            }
            //NORMAL POST
            1 -> {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_post_card, viewGroup, false)
                return PostInClubViewHolder(view)
            }
            //DEFAULT IS NORMAL POST
            else ->{
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_post_card, viewGroup, false)
                return PostInClubViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType) {
            //EVENT POST
            0 -> {
                val viewHolder: EventPostInClubViewHolder = holder as EventPostInClubViewHolder
//                viewHolder.postersUsername.text = SupabaseSingleton.getDisplayName(dataSet[position].userId)
                val postDto = dataSet[position]
                viewHolder.postersUsername.text = SupabaseSingleton.getDisplayName(postDto.userId)
                viewHolder.description.text = postDto.body
                viewHolder.timeSincePosted.text = postDto.createdAt
                viewHolder.eventLocation.text = postDto.eventLocation
                viewHolder.eventTime.text = postDto.eventTimeStart
                viewHolder.postTitle.text = postDto.title

                val eventId = postDto.eventId

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
                val viewHolder: PostInClubViewHolder = holder as PostInClubViewHolder
//                viewHolder.postersUsername.text =  SupabaseSingleton.getDisplayName(dataSet[position].userId)
                viewHolder.postersUsername.text = SupabaseSingleton.getDisplayName(dataSet[position].userId)
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.postTitle.text=dataSet[position].title
                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }
                setAnimation(viewHolder.itemView, position);
            }
            //DEFAULT IS NORMAL POST
            else ->{
                val viewHolder: PostInClubViewHolder = holder as PostInClubViewHolder
                viewHolder.postersUsername.text = SupabaseSingleton.getDisplayName(dataSet[position].userId)
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.postTitle.text=dataSet[position].title
                viewHolder.cardView.setOnClickListener {
                    goToPostPage(dataSet[position].id)
                }
                setAnimation(viewHolder.itemView, position);
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

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (String) -> Unit) {
        goToPostPage = callback
    }

    @SuppressLint("NotifyDataSetChanged")
    public fun updateDataSet(newData:  ArrayList<PostDto>){
        //call when the data changes.
        this.dataSet.clear()
        this.dataSet.addAll(newData)
        notifyDataSetChanged();
    }

}
