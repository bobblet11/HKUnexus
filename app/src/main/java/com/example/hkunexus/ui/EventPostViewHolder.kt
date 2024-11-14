package com.example.hkunexus.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R

// Use in couple with event_joining_button layout
open class JoinableEventPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val eventButtonsView = view.findViewById<View>(R.id.event_action_buttons)
}
