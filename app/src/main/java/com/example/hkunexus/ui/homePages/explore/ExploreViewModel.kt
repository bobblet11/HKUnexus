package com.example.hkunexus.ui.homePages.explore

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hkunexus.data.model.Club

class ExploreViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is explore Fragment"
    }

    // TODO: Hook up to supabase
    val clubs: Array<Club> = arrayOf(
        Club("Club 1", "Club 1 Desc", false),
        Club("Club 2", "Club 2 Desc", false),
        Club("Club 3", "Club 3 Desc", false)
    )

    val text: LiveData<String> = _text

    // I think there needs to be some checks to make sure that
    // the user has really joined / left the club
    // Before toggling the join / leave button, but whatever

    fun joinClub(position: Int) {
        // TODO: Hook up to supabase

        clubs[position].joined = true
    }

    fun leaveClub(position: Int) {
        // TODO: Hook up to supabase

        clubs[position].joined = false
    }
}