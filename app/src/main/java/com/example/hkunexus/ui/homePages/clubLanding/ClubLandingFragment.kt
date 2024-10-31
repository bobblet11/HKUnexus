package com.example.hkunexus.ui.homePages.clubLanding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.hkunexus.databinding.FragmentGroupLandingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClubLandingFragment : Fragment() {

    private val viewModel: ClubLandingViewModel by viewModels()
    private var _binding: FragmentGroupLandingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGroupLandingBinding.inflate(inflater, container, false)

        val groupLandingPostsRecycler = binding.groupLandingPostsRecycler
        val clubNameView = binding.clubName
        val clubDescriptionView = binding.clubDescription
        val joinButton = binding.clubJoinButton
        val leaveButton = binding.clubLeaveButton
        val postListAdapter = PostInClubListAdapter(arrayListOf())
        //set the clubID and fetch required data using clubID
        Log.d("clubLandingFrag", arguments?.getString("clubID").toString())

        viewModel.setClubID(arguments?.getString("clubID"), context)

        postListAdapter.setPostPageCallBack {
            position: Int ->
            Toast.makeText(context, "Should go to post page $position", Toast.LENGTH_SHORT).show()
        }
        groupLandingPostsRecycler.adapter = postListAdapter

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Update UI based on the new state
                clubNameView.text = state.name
                clubDescriptionView.text = state.description
                joinButton.visibility = if (state.joined) View.GONE else View.VISIBLE
                leaveButton.visibility = if (state.joined) View.VISIBLE else View.GONE
            }
        }

        joinButton.setOnClickListener {
            viewModel.joinClub()
        }

        leaveButton.setOnClickListener {
            viewModel.leaveClub()
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiStatePosts.collect { state ->
                postListAdapter.updateDataSet(state.posts.toCollection(ArrayList()))
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}