package com.example.meetingscheduler.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meetingscheduler.data.MeetingScheduleDataModel
import com.example.meetingscheduler.data.database.DataBaseQuery
import com.example.meetingscheduler.data.database.DataBaseQuery.Callback
import java.util.*

/**
 * View model for MeetingList Fragment
 */
class MeetingListViewModel(val app: Application) : AndroidViewModel(app), Callback {

    private val dataBaseQuery = DataBaseQuery(app.applicationContext, this)
    val meetingScheduleLiveData = MutableLiveData<List<MeetingScheduleDataModel>>()
    val meetingDateCalendar = Calendar.getInstance()

    init {
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingDateCalendar)
    }

    override fun onMeetingScheduleFetched(meetingSchedules: List<MeetingScheduleDataModel>) {
        meetingScheduleLiveData.value = meetingSchedules
    }

    /**
     * method used to fetch next day schedule
     */
    fun getNextDaySchedule() {
        getDateWithOffset(1)
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingDateCalendar)
    }

    /**
     * method used to fetch prev day schedule
     */
    fun getPrevDaySchedule() {
        getDateWithOffset(-1)
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingDateCalendar)
    }


    private fun getDateWithOffset(day: Int) {
        meetingDateCalendar.add(Calendar.DATE, day)
    }

}