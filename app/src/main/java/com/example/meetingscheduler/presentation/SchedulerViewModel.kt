package com.example.meetingscheduler.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meetingscheduler.data.*
import com.example.meetingscheduler.data.database.DataBaseQuery
import com.example.meetingscheduler.data.database.DataBaseQuery.Callback
import java.util.*

class SchedulerViewModel(val app: Application) : AndroidViewModel(app), Callback {

    private val dataBaseQuery = DataBaseQuery(app.applicationContext, this)
    val meetingScheduleLiveData = MutableLiveData<List<MeetingDataModel>>()
    var calendar: Calendar = Calendar.getInstance()
    var calendarStartTime = StartTime(-1,-1)
    var calendarEndTime = EndTime(-1,-1)

    init {
        getDateWithOffset(0)
        dataBaseQuery.selectAllMeetingScheduleAtDate(calendar)
    }

    override fun onMeetingScheduleFetched(meetingDataModel: List<MeetingDataModel>) {
        meetingScheduleLiveData.value = meetingDataModel
    }
    /**
     * method used to fetch next day schedule
     */
    fun getNextDaySchedule() {
        getDateWithOffset(1)
        dataBaseQuery.selectAllMeetingScheduleAtDate(calendar)
    }

    /**
     * method used to fetch prev day schedule
     */
    fun getPrevDaySchedule() {
        getDateWithOffset(-1)
        dataBaseQuery.selectAllMeetingScheduleAtDate(calendar)
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

    fun updateMeeting(description: String) {
        dataBaseQuery.insertMeetingSchedule(
            MeetingDataModel(
                calendar,
                calendarStartTime,
                calendarEndTime,
                description
            )
        )
    }

    private fun getDateWithOffset(day: Int) {
        calendar.add(Calendar.DATE, day)
    }
}
