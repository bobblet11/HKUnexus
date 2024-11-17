package com.example.hkunexus.ui.homePages.create

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class CreateGroupViewModel: ViewModel() {

    data class MyGroupsUiState(
        val clubName: String = "",
        val clubDesc: String = "",
        val bannerImage: Uri? = null
    )

    private val _uiState = MutableStateFlow(MyGroupsUiState())
    val uiState: StateFlow<MyGroupsUiState> = _uiState.asStateFlow()

    fun setClubName(str: String) {
        _uiState.update {
            it.copy(
                clubName = str
            )
        }
    }

    fun setClubDesc(str: String) {
        _uiState.update {
            it.copy(
                clubDesc = str
            )
        }
    }

    fun setBannerImage(uri: Uri?) {
        _uiState.update {
            it.copy(
                bannerImage = uri
            )
        }
    }

    fun hasBannerImage(): Boolean {
        return uiState.value.bannerImage != null
    }

    fun canCreate(): Boolean {
        return uiState.value.clubName.trim().isNotEmpty()
    }

    fun create(context: Context?): Boolean {
        if (canCreate()) {
            val bannerImage = uiState.value.bannerImage
            if (bannerImage != null) {
                val inputStream = context!!.contentResolver.openInputStream(bannerImage)

                inputStream?.close()
            }

            // TODO: OwO
            return true
        }
        return false
    }

    fun reset() {
        _uiState.update {
            it.copy(
                clubName = "",
                clubDesc = "",
                bannerImage = null,
            )
        }
    }
}

