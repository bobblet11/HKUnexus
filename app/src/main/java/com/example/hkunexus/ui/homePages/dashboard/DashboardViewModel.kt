package com.example.hkunexus.ui.homePages.dashboard

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    val usernameFieldText: LiveData<String> = MutableLiveData<String>().apply {value = "Placeholder Name" }
    val bioFieldText: LiveData<String> = MutableLiveData<String>().apply {value = "Placeholder Bio" }
}