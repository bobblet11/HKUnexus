package com.example.hkunexus.ui.homePages.clubLanding

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.PostDto

public final class PostInClubListAdapter(private val dataSet: ArrayList<PostDto>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var goToPostPage: (Int) -> Unit = { postID: Int -> }

    override public fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous

        return if (dataSet[position].eventId == null) 1 else 0
    }

    override public fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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

    override public fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType){
            //EVENT POST
            0 -> {
                val viewHolder: EventPostInClubViewHolder = holder as EventPostInClubViewHolder
//                viewHolder.postersUsername.text = SupabaseSingleton.getDisplayName(dataSet[position].userId)
                viewHolder.postersUsername.text = dataSet[position].userId
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.eventLocation.text=dataSet[position].eventLocation
                viewHolder.eventTime.text=dataSet[position].eventTimeStart

                viewHolder.joinButton.visibility = View.VISIBLE
                viewHolder.leaveButton.visibility = View.INVISIBLE
                viewHolder.cardView.setOnClickListener {
                    goToPostPage(position)
                }
            }
            //NORMAL POST
            1 -> {
                val viewHolder: PostInClubViewHolder = holder as PostInClubViewHolder
//                viewHolder.postersUsername.text =  SupabaseSingleton.getDisplayName(dataSet[position].userId)
                viewHolder.postersUsername.text = dataSet[position].userId
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.cardView.setOnClickListener {
                    goToPostPage(position)
                }
            }
            //DEFAULT IS NORMAL POST
            else ->{
                val viewHolder: PostInClubViewHolder = holder as PostInClubViewHolder
                viewHolder.postersUsername.text = dataSet[position].userId
                viewHolder.description.text = dataSet[position].body
                viewHolder.timeSincePosted.text = dataSet[position].createdAt
                viewHolder.cardView.setOnClickListener {
                    goToPostPage(position)
                }
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (Int) -> Unit) {
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
