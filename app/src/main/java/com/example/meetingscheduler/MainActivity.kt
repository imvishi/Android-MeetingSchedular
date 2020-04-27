package com.example.meetingscheduler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.meetingscheduler.presentation.MeetingListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MeetingListFragment.newInstance())
                    .commitNow()
        }
    }
}
