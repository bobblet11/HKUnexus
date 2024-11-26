package com.example.hkunexus.ui.homePages.editUserProfile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.example.hkunexus.R
import com.example.hkunexus.data.UserSingleton
import com.example.hkunexus.databinding.FragmentEditUserProfileBinding

class EditUserProfileFragment : Fragment() {
    private var _binding: FragmentEditUserProfileBinding? = null
    private val viewModel: EditUserProfileViewModel by viewModels()
    private val binding get() = _binding!!

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")

            viewModel.setProfilePicture(uri)

            val inputStream = requireContext().contentResolver.openInputStream(uri)
            inputStream.use{
                    input ->
                viewModel.imageFile!!.outputStream().use{
                        output ->
                    input!!.copyTo(output)
                }
            }

            updateProfilePicture()
            inputStream?.close()

            Log.d("PhotoPicker", viewModel.imageFile.toString())
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUserProfileBinding.inflate(inflater, container, false)

        viewModel.initializeValues()
        initializeFields()
        initializeProfilePicture()


        val firstName = binding.firstName
        val lastName = binding.lastName
        val username = binding.username

        firstName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.setFirstName(s.toString())
                updateSaveButton()
                updateBorderColours()
            }
        })

        lastName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.setLastName(s.toString())
                updateSaveButton()
                updateBorderColours()
            }
        })

        username.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.setUsername(s.toString())
                updateSaveButton()
                updateBorderColours()
            }
        })

        binding.uploadProfilePicture.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.saveEditButton.setOnClickListener {
            updateBorderColours()

            val success = viewModel.updateProfile()
            if (success) {
                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()

                val bundle = Bundle()
                bundle.putBoolean("result", true)
                requireActivity().supportFragmentManager.setFragmentResult("updatedProfile", bundle)

                findNavController().popBackStack()
            }
        }

        binding.cancelEditButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun initializeFields() {
        binding.firstName.setText(viewModel.uiState.value.firstName)
        binding.lastName.setText(viewModel.uiState.value.lastName)
        binding.username.setText(viewModel.uiState.value.username)
    }

    private fun initializeProfilePicture() {
        val pfp = binding.profilePicture

        val imageURL = UserSingleton.userPfp
        Log.d("ImageURL", "Fetched URL: $imageURL") // Log the fetched URL

        val placeholderImage = R.drawable.placeholder_view_vector

        // Load image using Glide with RequestListener
        Glide.with(pfp.context)
            .load(imageURL) // Load the image from the URL
            .placeholder(placeholderImage) // Placeholder while loading
            .error(placeholderImage) // Error image if loading fails
//            .override(300, 200) // Resize to desired size (adjust as needed)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Enable caching
            .thumbnail(0.1f) // Load a smaller thumbnail first
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide the image container on error
                    Log.d("Glide", "Image load failed: ${e?.message}")
                    return false // Allow Glide to handle the error placeholder
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Show the image container when image is loaded successfully
                    pfp.visibility = View.VISIBLE
                    Log.d("Glide", "Image loaded successfully")
                    return false // Allow Glide to handle the resource
                }
            })
            .into(pfp) // Set the ImageView

    }

    private fun updateBorderColours() {
        updateBorderColour(binding.firstName, viewModel.isFirstNameValid())
        updateBorderColour(binding.lastName, viewModel.isLastNameValid())
        updateBorderColour(binding.username, viewModel.isUsernameValid())
    }

    private fun updateBorderColour(border: EditText, isValid:Boolean){
        val invalidBorder = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_border_red)
        val validBorder = ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_border_normal)
        border.background = if(isValid) validBorder else invalidBorder
    }

    private fun updateProfilePicture() {
        if (viewModel.uiState.value.hasProfileImageChanged) {
            binding.profilePicture.setImageURI(
                viewModel.uiState.value.profilePicture
            )
        }
    }

    private fun updateSaveButton() {
        binding.saveEditButton.isEnabled = viewModel.isValid()
    }
}