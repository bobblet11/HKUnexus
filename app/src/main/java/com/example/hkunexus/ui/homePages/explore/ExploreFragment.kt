package com.example.hkunexus.ui.homePages.explore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.GroupLandingActivity
import com.example.hkunexus.databinding.FragmentExploreBinding
import com.example.hkunexus.ui.login.LoginActivityViewModel

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
        clubListAdapter.setLandingCallback {
                position: Int ->
            Toast.makeText(context, "Should go to landing page $position", Toast.LENGTH_SHORT).show()

            requireActivity().run{
                startActivity(Intent(activity, GroupLandingActivity::class.java))
            }

        }
        binding.exploreClubsRecycler.adapter = clubListAdapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}