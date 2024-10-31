package com.example.hkunexus.ui.homePages.clubLanding

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Post
import com.example.hkunexus.data.model.dto.PostDto

class PostInClubListAdapter(private val dataSet: ArrayList<PostDto>) :
    RecyclerView.Adapter<PostInClubViewHolder>() {
    private var goToPostPage: (Int) -> Unit = { postID: Int -> }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostInClubViewHolder {
        //creates the PostInCLubViewHolder i.e. a post card/event post card.
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_post_event_card, viewGroup, false)

        return PostInClubViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: PostInClubViewHolder, position: Int) {

        //here i need to assign the values of the card differently depending on whether the post is EVENT or NORMAL

//        if (dataSet[position].isEvent){
//            //create event post instead
//        }

        viewHolder.postersUsername.text = dataSet[position].userId
        viewHolder.compactDescription.text = dataSet[position].body
        viewHolder.timeSincePosted.text = dataSet[position].createdAt

        viewHolder.cardView.setOnClickListener {
            goToPostPage(position)
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
