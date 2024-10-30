package com.example.hkunexus.ui.homePages.explore

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.data.TempData
import com.example.hkunexus.data.model.Club
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class ExploreUiState(
    val listOfClubsToDisplay: Array<Club> = arrayOf(),
    val listOfTags: Array<String> = arrayOf(),

    val clubListAdapter: ExploreListAdapter? = null,
    val recyclerView: RecyclerView? = null,
)

class ExploreViewModel : ViewModel() {

    private val MAX_NUM_CHAR_IN_CLUB_TITLE: Int = 30
    private val MAX_NUM_CHAR_IN_CLUB_DESCRIPTION: Int =80

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        fetchClubs()
        fetchTags()
    }

    private fun fetchClubs(){
        // TODO: FETCH USING SUPABASE

        val tempList = TempData.clubs

        //replace templist with Supabase list

        //format club data to fit in card

        for (item: Club in tempList) {
            if (item.name.length >= MAX_NUM_CHAR_IN_CLUB_TITLE){
                val newName = item.name.substring(0,MAX_NUM_CHAR_IN_CLUB_TITLE-4) + "..."
                item.name = newName
            }

            if (item.description.length >= MAX_NUM_CHAR_IN_CLUB_DESCRIPTION){
                val newDescription = item.description.substring(0,MAX_NUM_CHAR_IN_CLUB_DESCRIPTION-4) + "..."
                item.description = newDescription
            }
        }

        _uiState.update {
            it.copy(
                listOfClubsToDisplay = tempList
            )
        }
    }

    private fun fetchTags() {
        val tempList: Array<String> = TempData.tags.clone()

        val tags: MutableList<String> = tempList.toMutableList()
        tags.add(0, "Any")

        _uiState.update {
            it.copy(
                listOfTags = tags.toTypedArray()
            )
        }
    }

}