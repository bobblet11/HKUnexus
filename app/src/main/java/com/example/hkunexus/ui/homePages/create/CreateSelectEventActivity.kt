package com.example.hkunexus.ui.homePages.create

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hkunexus.R
import com.example.hkunexus.ui.homePages.create.ui.createselectclub.CreateSelectEventFragment

class CreateSelectEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_select_event)

        var selectedClub: String? = ""
        selectedClub = intent?.extras?.getString("selectedClub");

        Log.d("create select event activity",selectedClub.toString())

        val frag  = CreateSelectEventFragment()
        frag.arguments=intent?.extras
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, frag)
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