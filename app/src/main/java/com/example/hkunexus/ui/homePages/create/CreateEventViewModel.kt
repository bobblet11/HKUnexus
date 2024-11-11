package com.example.hkunexus.ui.homePages.create

import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.model.dto.ClubDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class CreateEventViewModel: ViewModel() {

    data class UiState(
        val selectedClub: ClubDto? = null,
        val startDate: Calendar = Calendar.getInstance(),
        val endDate: Calendar = Calendar.getInstance(),
        val eventTitle: String = "",
        val eventDesc: String = "",
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val newEndDate = Calendar.getInstance()
        newEndDate.add(Calendar.HOUR, 1)

        _uiState.update {
            it.copy(
                endDate = newEndDate
            )
        }
    }

    fun setSelectedClub(club: ClubDto?) {

        _uiState.update {
            it.copy(
                selectedClub = club
            )
        }

    }

    fun setStartTime(h: Int, m: Int) {
        setTime(1, h, m)
    }

    fun setStartDate(y: Int, m: Int, d: Int) {
        setDate(1, y, m, d)
    }

    fun setEndTime(h: Int, m: Int) {
        setTime(2, h, m)
    }

    fun setEndDate(y: Int, m: Int, d: Int) {
        setDate(2, y, m, d)
    }

    private fun setTime(i: Int, hr: Int, min: Int) {

        when (i) {
            1 -> {
                val newDate: Calendar = _uiState.value.startDate
                newDate.set(Calendar.HOUR_OF_DAY, hr)
                newDate.set(Calendar.MINUTE, min)

                _uiState.update {
                    it.copy(
                        startDate = newDate
                    )
                }
            }
            2 -> {
                val newDate: Calendar = _uiState.value.endDate
                newDate.set(Calendar.HOUR_OF_DAY, hr)
                newDate.set(Calendar.MINUTE, min)

                _uiState.update {
                    it.copy(
                        endDate = newDate
                    )
                }
            }
        }



    }

    private fun setDate(i: Int, y: Int, m: Int, d: Int) {

        when (i) {
            1 -> {
                val newDate: Calendar = _uiState.value.startDate
                newDate.set(Calendar.YEAR, y)
                newDate.set(Calendar.MONTH, m)
                newDate.set(Calendar.DAY_OF_MONTH, d)

                _uiState.update {
                    it.copy(
                        startDate = newDate
                    )
                }
            }
            2 -> {
                val newDate: Calendar = _uiState.value.endDate
                newDate.set(Calendar.YEAR, y)
                newDate.set(Calendar.MONTH, m)
                newDate.set(Calendar.DAY_OF_MONTH, d)

                _uiState.update {
                    it.copy(
                        endDate = newDate
                    )
                }
            }
        }

    }

    fun isValidDate(): Boolean {
        return _uiState.value.startDate.timeInMillis < _uiState.value.endDate.timeInMillis
    }

    private fun isValidTitle(): Boolean {
        return _uiState.value.eventTitle.trim().isNotEmpty()
    }
    
    fun setTitle(title: String) {
        _uiState.update {
            it.copy(
                eventTitle = title
            )
        }
    }

    fun setDesc(desc: String) {
        _uiState.update {
            it.copy(
                eventDesc = desc
            )
        }
    }

    fun canPost(): Boolean {
        return _uiState.value.selectedClub != null
                && isValidDate()
                && isValidTitle()
    }

    fun post() {
        if (canPost()) {
            // TODO: Hook up to Supabase
        }
    }
}

