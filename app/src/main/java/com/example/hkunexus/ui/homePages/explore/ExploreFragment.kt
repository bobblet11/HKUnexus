package com.example.hkunexus.ui.homePages.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.R
import com.example.hkunexus.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {

    private val viewModel: ExploreViewModel by viewModels()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExploreBinding.inflate(inflater, container, false)


        val clubListAdapter = ClubListAdapter(viewModel.uiState.value.listOfClubsToDisplay)
        clubListAdapter.setLandingCallback ({ position: Int ->
            val b = Bundle()
            b.putInt("clubId", position)

            findNavController().navigate(R.id.action_view_group_landing, b)
        })
        binding.exploreClubsRecycler.adapter = clubListAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}