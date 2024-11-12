package com.example.hkunexus.ui.homePages.mygroups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.R
import com.example.hkunexus.databinding.FragmentMyGroupsBinding
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
        groupListAdapter.setPostPageCallBack ({ clubId: String ->
            val b = Bundle()
            b.putString("clubID", clubId)

            findNavController().navigate(R.id.navigation_group_landing, b)

        })
        binding.myGroupsClubsRecycler.adapter = groupListAdapter


        val swipeRefreshLayout = binding.refreshLayout

        // Refresh function for the layout
        swipeRefreshLayout.setOnRefreshListener{

            // Your code goes here
            // In this code, we are just changing the text in the
            // textbox

            viewModel.fetchMyGroups()

            // This line is important as it explicitly refreshes only once
            // If "true" it implicitly refreshes forever
            swipeRefreshLayout.isRefreshing = false
        }


        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}