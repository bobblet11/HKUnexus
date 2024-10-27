package com.example.hkunexus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hkunexus.ui.groupLanding.GroupLandingFragment

class GroupLandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_landing)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GroupLandingFragment.newInstance())
                .commitNow()
        }
    }
}