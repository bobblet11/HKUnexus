package com.example.hkunexus.ui.homePages.clubLanding

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Post
import android.view.ViewGroup as ViewGroup

class PostListAdapter(private val dataSet: Array<Post>) :
    RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    private var goToPostPage: (Int) -> Unit = { postID: Int -> }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var postersUsername = view.findViewById<TextView>(R.id.postersUsername)
        var compactDescription= view.findViewById<TextView>(R.id.compactEventpostDescription)
        var timeSincePosted = view.findViewById<TextView>(R.id.timeSincePosted)
        val cardView: CardView = view.findViewById(R.id.compactEventPostCard)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_post_event_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.postersUsername.text = dataSet[position].posterUsername
        viewHolder.compactDescription.text = dataSet[position].postText
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
