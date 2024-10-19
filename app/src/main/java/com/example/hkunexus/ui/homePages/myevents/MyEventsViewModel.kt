package com.example.hkunexus.ui.homePages.myevents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyEventsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is my events Fragment"
    }
    val text: LiveData<String> = _text
}