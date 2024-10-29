package com.example.hkunexus.ui.homePages.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.R
import com.example.hkunexus.databinding.FragmentExploreBinding

class ExploreFragment : Fragment(), AdapterView.OnItemSelectedListener  {

    private val viewModel: ExploreViewModel by viewModels()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        val searchView = binding.clubSearchBar
        searchView.isIconifiedByDefault = false

        constructClubTagAdaptor()

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

    private fun constructClubTagAdaptor() {
        val tags = viewModel.uiState.value.listOfTags

        val spinner: Spinner = binding.clubTagsSelector
        spinner.onItemSelectedListener = this

        val adaptor = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tags)
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptor
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
//        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
//        TODO("Not yet implemented")
    }


}