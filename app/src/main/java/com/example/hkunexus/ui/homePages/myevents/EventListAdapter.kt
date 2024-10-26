package com.example.hkunexus.ui.homePages.myevents

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.data.model.Event
import android.view.ViewGroup as ViewGroup

class CardListAdapter(private val dataSet: Array<Event>) :
    RecyclerView.Adapter<CardListAdapter.ViewHolder>() {

    private var goToPostPage: (Int) -> Unit = { postID: Int -> }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.club_card)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        viewHolder.cardView.setOnClickListener {
            goToPostPage(position)
        }
    }

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (Int) -> Unit) {
        goToPostPage = callback
    }
}
