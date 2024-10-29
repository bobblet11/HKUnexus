package com.example.hkunexus.ui.homePages.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.R
import com.example.hkunexus.databinding.FragmentExploreBinding


class ExploreFragment : Fragment()  {

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


        configureSearchBar(clubListAdapter)
        constructClubTagAdaptor(clubListAdapter)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureSearchBar(clubListAdapter: ClubListAdapter) {
        val searchView = binding.clubSearchBar
        searchView.isIconifiedByDefault = false

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(kw: String): Boolean {
                clubListAdapter.setFilterKeyword(kw)
                return false
            }

            override fun onQueryTextSubmit(kw: String): Boolean {
                clubListAdapter.setFilterKeyword(kw)
                return false
            }
        })
    }

    private fun constructClubTagAdaptor(clubListAdapter: ClubListAdapter) {
        val tags = viewModel.uiState.value.listOfTags

        val spinner: Spinner = binding.clubTagsSelector
        spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                    clubListAdapter.setSelectedTag(null)
                } else {
                    clubListAdapter.setSelectedTag(viewModel.uiState.value.listOfTags[p2])
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                clubListAdapter.setSelectedTag(null)
            }
        })

        val adaptor = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tags)
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptor
    }


}