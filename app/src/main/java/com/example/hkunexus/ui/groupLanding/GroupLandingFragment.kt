package com.example.hkunexus.ui.groupLanding

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.hkunexus.R

import com.example.hkunexus.databinding.FragmentGroupLandingBinding
import com.example.hkunexus.ui.homePages.dashboard.DashboardViewModel

class GroupLandingFragment(private val clubId: Int) : Fragment() {

    private var _binding: FragmentGroupLandingBinding? = null
    private val viewModel: GroupLandingViewModel by viewModels()
    private val binding get() = _binding!!

    companion object {
        fun newInstance(clubId: Int) = GroupLandingFragment(clubId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGroupLandingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.fetchClubData(clubId)

        val clubNameView: TextView = binding.clubName
        val clubDescView: TextView = binding.clubDescription

        clubNameView.text = viewModel.club.name
        clubDescView.text = viewModel.club.description

        val postListAdapter = PostListAdapter(viewModel.uiState.value.posts)

        postListAdapter.setPostPageCallBack {
                position: Int ->
            Toast.makeText(context, "Should go to post page $position", Toast.LENGTH_SHORT).show()
        }

        binding.groupLandingPostsRecycler.adapter = postListAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}