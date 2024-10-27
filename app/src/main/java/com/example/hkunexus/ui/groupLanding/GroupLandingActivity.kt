package com.example.hkunexus.ui.groupLanding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.hkunexus.R

class GroupLandingActivity : AppCompatActivity() {

    private val viewModel: GroupLandingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setContentView(R.layout.activity_group_landing)

        val clubNameView = findViewById<TextView>(R.id.club_name)
        val clubDescView = findViewById<TextView>(R.id.club_description)

        clubNameView.text = viewModel.club.name
        clubDescView.text = viewModel.club.description

        val groupLandingPostsRecycler = findViewById<RecyclerView>(R.id.group_landing_posts_recycler)

        val postListAdapter = PostListAdapter(viewModel.uiState.value.posts)

        postListAdapter.setPostPageCallBack {
                position: Int ->
            Toast.makeText(this, "Should go to post page $position", Toast.LENGTH_SHORT).show()
        }

        groupLandingPostsRecycler.adapter = postListAdapter
    }
}