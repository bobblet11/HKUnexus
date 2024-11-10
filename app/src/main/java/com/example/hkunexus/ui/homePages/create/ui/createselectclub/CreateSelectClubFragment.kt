package com.example.hkunexus.ui.homePages.create.ui.createselectclub

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.databinding.FragmentCreateSelectClubBinding

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


        val recycler: RecyclerView = binding.clubListRecycler
        val adapter = ClubListAdapter(
            viewModel.uiState.value.listOfGroupsToDisplay
        )

        adapter.setSelectCallback {
            clubDto: ClubDto ->
            val intent = Intent()
            intent.putExtra("selectedClub", clubDto)
            activity?.setResult(RESULT_OK, intent)
            activity?.finish()
        }

        recycler.adapter = adapter

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}