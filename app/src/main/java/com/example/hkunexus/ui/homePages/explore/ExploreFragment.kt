package com.example.hkunexus.ui.homePages.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.R
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.databinding.FragmentExploreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExploreFragment : Fragment()  {

    private val viewModel: ExploreViewModel by viewModels()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    val tags = arrayListOf("")
    private val exploreListAdapter = ExploreListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        exploreListAdapter.setLandingCallback ({ clubId: String ->
            val b = Bundle()
            b.putString("clubID", clubId)
            findNavController().navigate(R.id.navigation_group_landing, b)
        })

        binding.exploreClubsRecycler.adapter = exploreListAdapter

        configureSearchBar(exploreListAdapter)
        constructClubTagAdaptor(exploreListAdapter)

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Update UI based on the new state
                tags.clear()
                tags.addAll(state.listOfTags)
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { state ->
                exploreListAdapter.updateDataSet(state.listOfClubsToDisplay.toCollection(ArrayList()))
                exploreListAdapter.updateFilteredList()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureSearchBar(exploreListAdapter: ExploreListAdapter) {
        val searchView = binding.clubSearchBar
        searchView.isIconifiedByDefault = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(kw: String): Boolean {
                exploreListAdapter.setFilterKeyword(kw)
                return false
            }

            override fun onQueryTextSubmit(kw: String): Boolean {
                exploreListAdapter.setFilterKeyword(kw)
                return false
            }
        })
    }

    private fun constructClubTagAdaptor(exploreListAdapter: ExploreListAdapter) {
        val spinner: Spinner = binding.clubTagsSelector
        spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                    exploreListAdapter.setSelectedTag(null)
                } else {
                    exploreListAdapter.setSelectedTag(viewModel.uiState.value.listOfTags[p2])
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                exploreListAdapter.setSelectedTag(null)
            }
        })

        val adaptor = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tags)
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptor
    }


}