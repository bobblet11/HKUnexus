package com.example.hkunexus.ui.homePages.create.ui.createselectclub

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.SupabaseSingleton
import com.example.hkunexus.data.model.dto.ClubDto
import com.example.hkunexus.data.model.dto.EventDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateSelectEventUiState(
    val selectedClub: String? = null,
    val listOfEventsToDisplay: ArrayList<EventDto> = arrayListOf()
)


class CreateSelectEventViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CreateSelectEventUiState())
    val uiState: StateFlow<CreateSelectEventUiState> = _uiState.asStateFlow()

    init{
        Log.d("TESTER", "INIT")
    }
    private fun fetchEvents() {
        Log.d("test", uiState.value.selectedClub!!.toString())
        val tempList = SupabaseSingleton.getEventsFromClub(uiState.value.selectedClub!!)

        _uiState.update {
            it.copy(
                listOfEventsToDisplay = tempList.toCollection(ArrayList())
            )
        }

        Log.d("test12321321", uiState.value.listOfEventsToDisplay.toString())

    }

    fun setSelectedClubId(id: String?){
        _uiState.update {
            it.copy(
                selectedClub = id
            )
        }
        fetchEvents()
        Log.d("HERE", uiState.value.selectedClub!!.toString())
    }
}