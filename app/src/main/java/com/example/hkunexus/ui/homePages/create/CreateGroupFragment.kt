package com.example.hkunexus.ui.homePages.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hkunexus.databinding.FragmentCreateGroupBinding


class CreateGroupFragment : Fragment() {
    private var _binding: FragmentCreateGroupBinding? = null
    private val viewModel: CreateGroupViewModel by viewModels()
    private val binding get() = _binding!!

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            viewModel.setBannerImage(uri)
            updateBannerPhoto()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateGroupBinding.inflate(inflater, container, false)

        val clubName = binding.clubName
        val clubDesc = binding.clubDesc

        clubName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.setClubName(s.toString())
                updateCreateButton()
            }
        })

        clubDesc.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.setClubDesc(s.toString())
                updateCreateButton()
            }
        })

        binding.createClubButton.setOnClickListener {
            val success = viewModel.create(context)
            if (success) {
                viewModel.reset()
                updateAllFromViewModel()
                Toast.makeText(context, "Club created!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.uploadBannerImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.removeBannerImage.setOnClickListener {
            viewModel.setBannerImage(null)
            updateBannerPhoto()
        }

        updateBannerPhoto()
        updateCreateButton()

        return binding.root
    }

    private fun updateBannerPhoto() {
        binding.bannerImage.setImageURI(
            viewModel.uiState.value.bannerImage
        )

        if (viewModel.hasBannerImage()) {
            binding.bannerImage.visibility = View.VISIBLE
            binding.removeBannerImage.visibility = View.VISIBLE
        } else {
            binding.bannerImage.visibility = View.GONE
            binding.removeBannerImage.visibility = View.GONE
        }
    }

    private fun updateCreateButton() {
        binding.createClubButton.isEnabled = viewModel.canCreate()
    }

    private fun updateAllFromViewModel() {
        binding.clubName.setText(viewModel.uiState.value.clubName)
        binding.clubDesc.setText(viewModel.uiState.value.clubDesc)
        updateBannerPhoto()
        updateCreateButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}