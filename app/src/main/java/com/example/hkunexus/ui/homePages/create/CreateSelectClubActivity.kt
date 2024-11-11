package com.example.hkunexus.ui.homePages.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.ui.homePages.create.ui.createselectclub.CreateSelectClubFragment

class CreateSelectClubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_select_club)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CreateSelectClubFragment())
                .commitNow()
        }

//        actionBar?.setDisplayHomeAsUpEnabled(true);
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                NavUtils.navigateUpFromSameTask(this)
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item);
//        }
//    }
}