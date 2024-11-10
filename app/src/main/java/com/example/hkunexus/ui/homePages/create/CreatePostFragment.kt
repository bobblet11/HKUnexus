package com.example.hkunexus.ui.homePages.create

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.databinding.FragmentCreatePostBinding


class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null
    private val viewModel: CreatePostViewModel by viewModels()
    private val binding get() = _binding!!

    private val requestSelectedClubLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val clubSerializable = result.data?.getSerializableExtra("selectedClub")
            val club = if (clubSerializable == null) {
                null
            } else {
                clubSerializable as ClubDto
            }

            viewModel.setSelectedClub(club)

            val clubText = binding.selectedClub
            if (club == null) {
                clubText.text = "Current: ---"
            } else {
                clubText.text = "Current: " + club.clubName
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)

        val clubButton: Button = binding.selectClubButton
        clubButton.setOnClickListener {
            val intent: Intent = Intent(activity, CreateSelectClubActivity::class.java)
            requestSelectedClubLauncher.launch(intent)
        }
        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}