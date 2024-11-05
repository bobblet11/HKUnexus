package com.example.hkunexus.ui.homePages.mygroups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.hkunexus.databinding.FragmentMyEventsBinding
import com.example.hkunexus.databinding.FragmentMyGroupsBinding
import com.example.hkunexus.ui.homePages.myevents.EventListAdapter
import com.example.hkunexus.ui.homePages.myevents.MyEventsViewModel
import com.example.hkunexus.ui.homePages.myevents.MyGroupsViewModel

class MyGroupsFragment : Fragment() {

    private var _binding: FragmentMyGroupsBinding? = null
    private val viewModel: MyGroupsViewModel by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyGroupsBinding.inflate(inflater, container, false)
        val groupListAdapter = GroupListAdapter(viewModel.uiState.value.listOfGroupsToDisplay)
        groupListAdapter.setPostPageCallBack {
                position: Int ->
            Toast.makeText(context, "Should go to post page $position", Toast.LENGTH_SHORT).show()
        }
        binding.myGroupsClubsRecycler.adapter = groupListAdapter

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}