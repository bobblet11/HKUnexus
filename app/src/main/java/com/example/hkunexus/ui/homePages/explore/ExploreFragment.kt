package com.example.hkunexus.ui.homePages.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exploreViewModel =
            ViewModelProvider(this).get(ExploreViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up adaptor for recycler view to display cards
        val clubListAdapter = ClubListAdapter(exploreViewModel.clubs)
        clubListAdapter.setJoinCallback(exploreViewModel::joinClub)
        clubListAdapter.setLeaveCallback(exploreViewModel::leaveClub)

        val recyclerView: RecyclerView = binding.exploreClubsRecycler
        recyclerView.adapter = clubListAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}