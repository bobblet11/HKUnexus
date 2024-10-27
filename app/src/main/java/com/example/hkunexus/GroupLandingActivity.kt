package com.example.hkunexus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hkunexus.ui.groupLanding.GroupLandingFragment

class GroupLandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val b = intent.extras
        var clubId = 0
        if (b != null) {
            clubId = b.getInt("clubID")
        } else {
            Toast.makeText(this,
                "Please pass a club ID while opening this page, defaulting to 0 now",
                Toast.LENGTH_SHORT).show()
        }

        setContentView(R.layout.activity_group_landing)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GroupLandingFragment.newInstance(clubId))
                .commitNow()
        }
    }
}