package com.example.hkunexus.ui.homePages.clubLanding.users

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.ui.JoinableEventPostViewHolder

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view){
    var userPfp = view.findViewById<ImageView>(R.id.userPfp)
    var username = view.findViewById<TextView>(R.id.user_name)
    var background = view.findViewById<CardView>(R.id.club_card)
}
