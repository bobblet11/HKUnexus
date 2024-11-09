package com.example.hkunexus.ui.homePages.myevents

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hkunexus.databinding.FragmentMyEventsListBinding


class MyEventsListFragment : Fragment() {
    private var _binding: FragmentMyEventsListBinding? = null
    private val viewModel: MyEventsViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsListBinding.inflate(inflater, container, false)
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