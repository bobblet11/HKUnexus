package com.example.hkunexus.ui.homePages.create

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.EventDto
import com.example.hkunexus.databinding.FragmentCreatePostBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

            viewModel.setIsClubSelected(true)
            viewModel.setSelectedClub(club)

            val clubText = binding.selectedClub
            if (club == null) {
                clubText.text = "Selected Club: ---"
            } else {
                clubText.text = "Selected Club: " + club.clubName
            }

        }
    }

    private val requestSelectedEventLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val eventSerializable = result.data?.getSerializableExtra("selectedEvent")
            val event = if (eventSerializable == null) {
                null
            } else {
                eventSerializable as EventDto
            }

            viewModel.setIsEventSelected(true)
            viewModel.setSelectedEvent(event)

            val eventText = binding.selectedEvent
            if (event == null) {
                eventText.text = "Selected Event: ---"
            } else {
                eventText.text = "Selected Event: " + event.title
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

        val eventSwitch = binding.switch1
        val eventSelectText = binding.selectedEvent
        val eventSelectButton = binding.selectEventButton
        val postButton = binding.createPostButton

        eventSwitch.setClickable(false);
        eventSwitch.setAlpha(0.5f)
        eventSelectButton.setEnabled(false)

        eventSelectButton.setOnClickListener {
            val intent: Intent = Intent(activity, CreateSelectEventActivity::class.java)
            intent.putExtra("selectedClub", viewModel.uiState.value.selectedClub?.clubId)
            Log.d("event intent", viewModel.uiState.value.selectedClub?.clubId.toString())
            requestSelectedEventLauncher.launch(intent)
        }

        eventSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            if (isChecked){
                viewModel.setIsEventPost(true)
            }else{
                viewModel.setIsEventPost(false)
                viewModel.setIsEventSelected(false)
                viewModel.setSelectedEvent(null)

                val eventText = binding.selectedEvent
                eventText.text = "Selected Event: ---"

            }
        })

        val postTitle = binding.Entertitle
        val postBody = binding.content

        postTitle.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
                viewModel.setPostTitle(s.toString())
            }
        })

        postBody.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
                viewModel.setPostBody(s.toString())
            }
        })




        CoroutineScope(Dispatchers.Main).launch {
            viewModel.uiState.collect { state ->
                eventSwitch.setClickable(state.isClubSelected)
                eventSwitch.setAlpha(if (state.isClubSelected) 1.0f else 0.5f)
                eventSelectButton.setEnabled(state.isClubSelected && state.isEventPost)
                postButton.setEnabled(state.isPostValid && state.isClubSelected && (if (state.isEventPost) state.isEventSelected else true))
            }
        }

        return binding.root

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}