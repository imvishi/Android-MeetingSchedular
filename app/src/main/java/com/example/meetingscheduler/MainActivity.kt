package com.example.meetingscheduler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.meetingscheduler.presentation.MeetingListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            showMeetingListFragment()
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0 ){
            super.onBackPressed()
        }
        supportFragmentManager.popBackStack()
        showMeetingListFragment()
    }

    /**
     * method to show meeting list fragment
     */
    private fun showMeetingListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MeetingListFragment.newInstance())
            .commitNow()
    }
}
