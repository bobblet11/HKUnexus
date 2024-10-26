package com.example.hkunexus.ui.homePages.explore

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.data.model.Club
import com.example.hkunexus.databinding.FragmentExploreBinding
import com.example.hkunexus.ui.login.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class ExploreUiState(
    val listOfClubsToDisplay: Array<Club> = arrayOf(),
    val clubListAdapter: ClubListAdapter? = null,
    val recyclerView: RecyclerView? = null,

)



class ExploreViewModel : ViewModel() {

    private val MAX_NUM_CHAR_IN_CLUB_TITLE: Int = 30
    private val MAX_NUM_CHAR_IN_CLUB_DESCRIPTION: Int =80

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        fetchClubs()
    }

    private fun fetchClubs(){
        //FETCH USING SUPABASE
        val tempList: Array<Club> = arrayOf(Club("BENS Tennis club!", "come join me and my friends in tennis! We are all beginners!"),
            Club("Mahjong Mondays", "Do you like mahjong? Do you hate Mondays? Then why don't you make your monday better with MAHJONG!"),
            Club("Genshin Impact learners", "wanna learn tips and tricks to max your character? come join our lessons!"))
        //replace templist with Supabase list

        //format club data to fit in card

        for (item: Club in tempList) {
            if (item.name.length >= MAX_NUM_CHAR_IN_CLUB_TITLE){
                var new_name = item.name.substring(0,MAX_NUM_CHAR_IN_CLUB_TITLE-4) + "..."
                item.name = new_name
            }

            if (item.description.length >= MAX_NUM_CHAR_IN_CLUB_DESCRIPTION){
                var new_description = item.description.substring(0,MAX_NUM_CHAR_IN_CLUB_DESCRIPTION-4) + "..."
                item.description = new_description
            }
        }

        _uiState.update {
            it.copy(
                listOfClubsToDisplay = tempList
            )
        }
    }

}