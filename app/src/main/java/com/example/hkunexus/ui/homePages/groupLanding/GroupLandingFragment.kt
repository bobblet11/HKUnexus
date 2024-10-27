package com.example.hkunexus.ui.homePages.groupLanding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hkunexus.databinding.FragmentGroupLandingBinding

class GroupLandingFragment : Fragment() {

    private val viewModel: GroupLandingViewModel by viewModels()
    private var _binding: FragmentGroupLandingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGroupLandingBinding.inflate(inflater, container, false)

        fetchClubDetails()

        loadClubText()
        loadClubJoinStatus()
        loadClubPosts()

        return binding.root
    }

    private fun fetchClubDetails() {
        var clubId = arguments?.getInt("clubId")
        if (clubId == null) {
            Toast.makeText(
                context,
                "Please pass a club ID while opening this page. Defaulting to 0...",
                Toast.LENGTH_SHORT
            ).show()
            clubId = 0
        }

        viewModel.fetchClubData(clubId)
    }

    private fun loadClubText() {
        val clubNameView = binding.clubName
        val clubDescView = binding.clubDescription

        clubNameView.text = viewModel.club.name
        clubDescView.text = viewModel.club.description
    }


    private fun loadClubJoinStatus() {
        val joinButton = binding.clubJoinButton
        val leaveButton = binding.clubLeaveButton

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
        val groupLandingPostsRecycler = binding.groupLandingPostsRecycler
        val postListAdapter = PostListAdapter(viewModel.uiState.value.posts)

        postListAdapter.setPostPageCallBack { position: Int ->
            Toast.makeText(context, "Should go to post page $position", Toast.LENGTH_SHORT).show()
        }

        groupLandingPostsRecycler.adapter = postListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}