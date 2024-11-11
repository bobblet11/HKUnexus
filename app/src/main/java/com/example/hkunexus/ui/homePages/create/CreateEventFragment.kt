package com.example.hkunexus.ui.homePages.create

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.databinding.FragmentCreateEventBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class DatePickerFragment(private val c: Calendar) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var dateSetCallback: (DatePicker, Int, Int, Int) -> Unit = { _, _, _, _ -> }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        dateSetCallback(view, year, month, day)
    }

    fun setDateSetCallback(callback: (DatePicker, Int, Int, Int) -> Unit) {
        dateSetCallback = callback
    }
}

class TimePickerFragment(private val c: Calendar) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var timeSetCallback: (TimePicker, Int, Int) -> Unit = { _, _, _ -> }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity, this, hour, minute, DateFormat.is24HourFormat(activity)
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        timeSetCallback(view, hourOfDay, minute)
    }

    fun setTimeSetCallback(callback: (TimePicker, Int, Int) -> Unit) {
        timeSetCallback = callback
    }
}

class CreateEventFragment : Fragment() {

    private var _binding: FragmentCreateEventBinding? = null
    private val viewModel: CreateEventViewModel by viewModels()
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
                clubText.text = "Selected club: ---"
            } else {
                clubText.text = "Selected club: " + club.clubName
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)

        setDateButtonEventListeners()
        updateDateTime()

        val eventTitle = binding.Entertitle
        val eventDesc = binding.content

        eventTitle.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.setTitle(s.toString())
            }
        })

        eventDesc.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.setDesc(s.toString())
            }
        })

        binding.createEventButton.setOnClickListener {
            viewModel.post()
        }

        updateCreateButton()

        val clubButton: Button = binding.selectClubButton
        clubButton.setOnClickListener {
            val intent = Intent(activity, CreateSelectClubActivity::class.java)
            requestSelectedClubLauncher.launch(intent)
        }
        return binding.root
    }

    private fun updateCreateButton() {
        binding.createEventButton.isEnabled = viewModel.canPost()
    }

    private fun setDateButtonEventListeners() {
        binding.selectStartDate.setOnClickListener {
            activity?.let { it1 ->
                val datePicker = DatePickerFragment(viewModel.uiState.value.startDate)
                datePicker.setDateSetCallback({ view: DatePicker, y: Int, m: Int, d: Int ->
                    viewModel.setStartDate(y, m, d)
                    updateDateTime()
                    updateCreateButton()
                })
                datePicker.show(it1.supportFragmentManager, "datePicker")
            }
        }

        binding.selectStartTime.setOnClickListener {
            activity?.let { it1 ->
                val timePicker = TimePickerFragment(viewModel.uiState.value.startDate)
                timePicker.setTimeSetCallback({ view: TimePicker, h: Int, m: Int ->
                    viewModel.setStartTime(h, m)
                    updateDateTime()
                    updateCreateButton()
                })
                timePicker.show(it1.supportFragmentManager, "timePicker")
            }
        }

        binding.selectEndDate.setOnClickListener {
            activity?.let { it1 ->
                val datePicker = DatePickerFragment(viewModel.uiState.value.endDate)
                datePicker.setDateSetCallback({ view: DatePicker, y: Int, m: Int, d: Int ->
                    viewModel.setEndDate(y, m, d)
                    updateDateTime()
                    updateCreateButton()
                })
                datePicker.show(it1.supportFragmentManager, "datePicker")
            }
        }

        binding.selectEndTime.setOnClickListener {
            activity?.let { it1 ->
                val timePicker = TimePickerFragment(viewModel.uiState.value.endDate)
                timePicker.setTimeSetCallback({ view: TimePicker, h: Int, m: Int ->
                    viewModel.setEndTime(h, m)
                    updateDateTime()
                    updateCreateButton()
                })
                timePicker.show(it1.supportFragmentManager, "timePicker")
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateDateTime() {

        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val timeFormat = if (DateFormat.is24HourFormat(activity)) {
            SimpleDateFormat("HH:mm")
        } else {
            SimpleDateFormat("hh:mm a")
        }
        val startDate: String = dateFormat.format(viewModel.uiState.value.startDate.time)
        val startTime: String = timeFormat.format(viewModel.uiState.value.startDate.time)
        val endDate: String = dateFormat.format(viewModel.uiState.value.endDate.time)
        val endTime: String = timeFormat.format(viewModel.uiState.value.endDate.time)
        binding.selectStartDate.text = startDate
        binding.selectStartTime.text = startTime
        binding.selectEndDate.text = endDate
        binding.selectEndTime.text = endTime

        val components = arrayOf(
            binding.startsText,
            binding.endsText
        )

        val defaultColour: Int = android.R.color.tab_indicator_text

        if (viewModel.isValidDate()) {
            for (el in components) {
                el.setTextColor(resources.getColor(defaultColour))
            }
        } else {
            for (el in components) {
                el.setTextColor(Color.RED)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}