package com.example.meetingscheduler.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meetingscheduler.data.*
import java.util.*

class SchedulerViewModel(val app: Application) : AndroidViewModel(app) {
    private val _meetingScheduleLiveData = MutableLiveData<List<MeetingDataModel>>()
    var calendar: Calendar = Calendar.getInstance()
    var calendarStartTime = StartTime(-1,-1)
    var calendarEndTime = EndTime(-1,-1)


    fun getMeetingScheduleLivaData() = _meetingScheduleLiveData

    /**
     * method used to fetch next day schedule
     */
    fun getNextDaySchedule() {
        getDateWithOffset(1)
    }

    /**
     * method used to fetch prev day schedule
     */
    fun getPrevDaySchedule() {
        getDateWithOffset(-1)
    }

    /**
     * Method to set the meeting date
     */
    fun setMeetingScheduleDate(calendar: Calendar) {
        this.calendar = calendar
    }


    /**
     * method to set the meeting time
     */
    fun setMeetingTime(time: MeetingTime) {
        when(time) {
            is StartTime -> calendarStartTime = time
            is EndTime -> calendarEndTime = time

        }
    }

    private fun getDateWithOffset(day: Int) {
        calendar.add(Calendar.DATE, day)
    }
}
