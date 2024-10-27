package com.example.hkunexus.ui.groupLanding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R

class GroupLandingActivity : AppCompatActivity() {

    private val viewModel: GroupLandingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchClubDetails()

        setContentView(R.layout.activity_group_landing)

        loadClubText()
        loadClubJoinStatus()
        loadClubPosts()
    }

    private fun fetchClubDetails() {
        val b = intent.extras

        var clubId = 0
        if (b != null) {
            clubId = b.getInt("clubId")
        } else {
            Toast.makeText(this,
                "Please pass a club ID while opening this page, defaulting to 0 now",
                Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchClubData(clubId)
    }

    private fun loadClubText() {
        val clubNameView = findViewById<TextView>(R.id.club_name)
        val clubDescView = findViewById<TextView>(R.id.club_description)

        clubNameView.text = viewModel.club.name
        clubDescView.text = viewModel.club.description
    }


    private fun loadClubJoinStatus() {
        val joinButton = findViewById<Button>(R.id.club_join_button)
        val leaveButton = findViewById<Button>(R.id.club_leave_button)

        fun updateButtonVisibility() {
            if (viewModel.club.joined) {
                joinButton.visibility = View.GONE
                leaveButton.visibility = View.VISIBLE
            } else {
                joinButton.visibility = View.VISIBLE
                leaveButton.visibility = View.GONE
            }
        }

        // TODO: Connect to Supabase
        joinButton.setOnClickListener {
            viewModel.club.joined = true
            updateButtonVisibility()
        }

        leaveButton.setOnClickListener {
            viewModel.club.joined = false
            updateButtonVisibility()
        }

        updateButtonVisibility()
    }

    private fun loadClubPosts() {
        val groupLandingPostsRecycler =
            findViewById<RecyclerView>(R.id.group_landing_posts_recycler)
        val postListAdapter = PostListAdapter(viewModel.uiState.value.posts)

        postListAdapter.setPostPageCallBack { position: Int ->
            Toast.makeText(this, "Should go to post page $position", Toast.LENGTH_SHORT).show()
        }

        groupLandingPostsRecycler.adapter = postListAdapter
    }
}