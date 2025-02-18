package com.example.hkunexus.ui.homePages.explore

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.R
import com.example.hkunexus.data.model.dto.Tag
import com.example.hkunexus.databinding.FragmentExploreBinding
import com.example.hkunexus.databinding.FragmentGroupLandingBinding
import com.example.hkunexus.ui.homePages.clubLanding.ClubLandingFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExploreFragment : Fragment()  {

    private val viewModel: ExploreViewModel by viewModels()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private val tags = arrayListOf("")
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

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { state ->
                exploreListAdapter.updateDataSet(
                    state.listOfClubsToDisplay.toCollection(ArrayList())
                )
                tags.clear()
                tags.addAll(state.listOfTags.map{t: Tag -> t.tagName})
            }
        }

        configureSearchBar()
        constructClubTagAdaptor()

        val swipeRefreshLayout = binding.refreshLayout

        // Refresh function for the layout
        swipeRefreshLayout.setOnRefreshListener{

            // Your code goes here
            // In this code, we are just changing the text in the
            // textbox

            viewModel.fetchClubs()
            viewModel.fetchTags()

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

    private fun configureSearchBar() {
        val searchView = binding.clubSearchBar
        searchView.isIconifiedByDefault = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // There won't be any update to the data (except when empty) to reduce cost
            override fun onQueryTextChange(kw: String): Boolean {
                if (TextUtils.isEmpty(kw)) {
                    viewModel.setKeyword(kw)
                    viewModel.fetchClubs()
                }
                return false
            }

            override fun onQueryTextSubmit(kw: String): Boolean {
//                exploreListAdapter.setFilterKeyword(kw)
                viewModel.setKeyword(kw)
                viewModel.fetchClubs()
                return false
            }
        })
    }

    private fun constructClubTagAdaptor() {
        val spinner: Spinner = binding.clubTagsSelector
        spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                    viewModel.setSelectedTagID(null)
                } else {
                    viewModel.setSelectedTagID(viewModel.uiState.value.listOfTags[p2].id)
                }
                viewModel.fetchClubs()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.setSelectedTagID(null)
                viewModel.fetchClubs()
            }
        })

        val adaptor = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tags)
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptor
    }


}