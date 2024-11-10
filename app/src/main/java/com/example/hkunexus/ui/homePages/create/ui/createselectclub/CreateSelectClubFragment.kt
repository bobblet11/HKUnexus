package com.example.hkunexus.ui.homePages.create.ui.createselectclub

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.databinding.FragmentCreateSelectClubBinding
import com.example.hkunexus.databinding.FragmentMyGroupsBinding
import com.example.hkunexus.ui.homePages.mygroups.GroupListAdapter

class CreateSelectClubFragment : Fragment() {
    private var _binding: FragmentCreateSelectClubBinding? = null
    private val viewModel: CreateSelectClubViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSelectClubBinding.inflate(inflater, container, false)

        val adapter = ClubListAdapter (
            viewModel.uiState.value.listOfGroupsToDisplay
        )

        Log.d("clubListAdapter", viewModel.uiState.value.listOfGroupsToDisplay.toString())

        adapter.setSelectCallback {
            clubDto: ClubDto ->
            val intent = Intent()
            intent.putExtra("selectedClub", clubDto)
            activity?.setResult(RESULT_OK, intent)
            activity?.finish()
        }

        binding.clubListRecycler.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}