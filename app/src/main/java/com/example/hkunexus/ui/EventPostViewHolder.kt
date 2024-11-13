package com.example.hkunexus.ui

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.SupabaseSingleton

open class JoinableEventPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val joinButton: Button = view.findViewById<Button>(R.id.joinEventButton)
    val leaveButton: Button = view.findViewById<Button>(R.id.leaveEventButton)
    private var joinCallback: () -> Unit = {}
    private var leaveCallback: () -> Unit = {}

    init {
        joinButton.setOnClickListener {
            joinCallback()
        }

        leaveButton.setOnClickListener {
            leaveCallback()
        }
    }


    fun setJoinCallback(callback: () -> Unit) {
        joinCallback = callback
    }

    fun setLeaveCallback(callback: () -> Unit) {
        leaveCallback = callback
    }
}
