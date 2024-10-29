package com.example.hkunexus.ui.homePages.myevents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.hkunexus.databinding.FragmentMyEventsBinding
import com.example.hkunexus.ui.homePages.explore.ClubListAdapter
import com.example.hkunexus.ui.homePages.explore.ExploreViewModel

class MyEventsFragment : Fragment() {

    private var _binding: FragmentMyEventsBinding? = null
    private val viewModel: MyEventsViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        val eventListAdapter = EventListAdapter(viewModel.uiState.value.listOfEventsToDisplay)
        eventListAdapter.setPostPageCallBack {
                position: Int ->
            Toast.makeText(context, "Should go to post page $position", Toast.LENGTH_SHORT).show()
        }
        binding.myEventsClubsRecycler.adapter = eventListAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}