package com.example.hkunexus.ui.homePages.create.ui.createselectclub

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hkunexus.data.model.dto.EventDto
import com.example.hkunexus.databinding.FragmentCreateSelectEventBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateSelectEventFragment : Fragment() {
    private var _binding: FragmentCreateSelectEventBinding? = null
    private val viewModel: CreateSelectEventViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSelectEventBinding.inflate(inflater, container, false)

        val adapter = EventListAdapter (
            arrayListOf()
        )
        var selectedClub: String? = null
        if (arguments != null) {
            selectedClub = requireArguments().getString("selectedClub")
        }

        viewModel.setSelectedClubId(selectedClub)

        Log.d("eventListAdapter", viewModel.uiState.value.listOfEventsToDisplay.toString())

        adapter.setSelectCallback {
            eventDto: EventDto ->
            val intent = Intent()
            intent.putExtra("selectedEvent", eventDto)
            activity?.setResult(RESULT_OK, intent)
            activity?.finish()
        }

        binding.eventListRecycler.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { state ->
                Log.d("w21321", state.listOfEventsToDisplay.toCollection(ArrayList()).toString())
                adapter.updateDataSet(state.listOfEventsToDisplay.toCollection(ArrayList()))
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}