package com.example.hkunexus.ui.homePages.mygroups

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hkunexus.R
import com.example.hkunexus.data.model.dto.Tag
import com.example.hkunexus.databinding.FragmentMyGroupsBinding
import com.example.hkunexus.ui.homePages.myevents.MyGroupsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyGroupsFragment : Fragment() {

    private var _binding: FragmentMyGroupsBinding? = null
    private val viewModel: MyGroupsViewModel by viewModels()
    private val binding get() = _binding!!
    private val tags = arrayListOf("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyGroupsBinding.inflate(inflater, container, false)
        val groupListAdapter = GroupListAdapter(arrayListOf(),requireContext())

        groupListAdapter.setPostPageCallBack ({ clubId: String ->
            val b = Bundle()
            b.putString("clubID", clubId)

            findNavController().navigate(R.id.navigation_group_landing, b)

        })

        binding.myGroupsClubsRecycler.adapter = groupListAdapter

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { state ->
                groupListAdapter.updateDataSet(
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

            viewModel.fetchMyClubs()
            viewModel.fetchTags()

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
            override fun onQueryTextChange(kw: String): Boolean {
                if (TextUtils.isEmpty(kw)) {
                    viewModel.setKeyword(kw)
                    viewModel.fetchMyClubs()
                }
                return false
            }

            override fun onQueryTextSubmit(kw: String): Boolean {
                viewModel.setKeyword(kw)
                viewModel.fetchMyClubs()
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
                viewModel.fetchMyClubs()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.setSelectedTagID(null)
                viewModel.fetchMyClubs()
            }
        })

        val adaptor = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tags)
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptor
    }
}