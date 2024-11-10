package com.example.hkunexus.ui.homePages.create

import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.model.dto.ClubDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyClubsUiState(
    val selectedClub: ClubDto? = null
)

class CreatePostViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MyClubsUiState())
    val uiState: StateFlow<MyClubsUiState> = _uiState.asStateFlow()

    fun setSelectedClub(club: ClubDto?) {

        _uiState.update {
            it.copy(
                selectedClub = club
            )
        }

    }
}

