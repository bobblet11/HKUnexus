package com.example.hkunexus.ui.homePages.explore

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Club
import android.view.ViewGroup as ViewGroup1


class ClubListAdapter(private val dataSet: Array<Club>) :
    RecyclerView.Adapter<ClubListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Define click listener for the ViewHolder's View
        val nameTextView: TextView = view.findViewById(R.id.club_name)
        val descTextView: TextView = view.findViewById(R.id.club_desc)
        val joinButtonView: Button = view.findViewById(R.id.club_join_button)
        val leaveButtonView: Button = view.findViewById(R.id.club_leave_button)

        // TODO: Hook up to supabase
        var joined: Boolean = false

        init {
            leaveButtonView.visibility = View.GONE

            joinButtonView.setOnClickListener {
                joined = true
                joinButtonView.visibility = View.GONE
                leaveButtonView.visibility = View.VISIBLE
            }

            leaveButtonView.setOnClickListener {
                joined = false
                joinButtonView.visibility = View.VISIBLE
                leaveButtonView.visibility = View.GONE
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup1, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nameTextView.text = dataSet[position].name
        viewHolder.descTextView.text = dataSet[position].desc
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
