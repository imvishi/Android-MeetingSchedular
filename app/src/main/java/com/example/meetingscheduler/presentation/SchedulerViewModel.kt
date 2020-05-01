package com.example.meetingscheduler.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.meetingscheduler.data.*
import com.example.meetingscheduler.data.MeetingTime.MEETING_END_TIME
import com.example.meetingscheduler.data.MeetingTime.MEETING_START_TIME
import com.example.meetingscheduler.data.database.DataBaseQuery
import com.example.meetingscheduler.data.database.DataBaseQuery.Callback
import com.example.meetingscheduler.utils.observeOnceAndNonNull
import java.util.*

class SchedulerViewModel(val app: Application) : AndroidViewModel(app), Callback {

    private val dataBaseQuery = DataBaseQuery(app.applicationContext, this)
    val meetingScheduleLiveData = MutableLiveData<List<MeetingDataModel>>()
    val meetingScheduledConfirmation = MutableLiveData<Boolean>()
    var meetingStartTime = MEETING_START_TIME
    var meetingEndTime = MEETING_END_TIME


    override fun onMeetingScheduleFetched(meetingDataModel: List<MeetingDataModel>) {
        meetingScheduleLiveData.value = meetingDataModel
    }

    /**
     * method to fetch current day schedule
     */
    fun getCurrentDaySchedule() {
        getDateWithOffset(0)
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingStartTime.calendar)
    }
    /**
     * method used to fetch next day schedule
     */
    fun getNextDaySchedule() {
        getDateWithOffset(1)
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingStartTime.calendar)
    }

    /**
     * method used to fetch prev day schedule
     */
    fun getPrevDaySchedule() {
        getDateWithOffset(-1)
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingStartTime.calendar)
    }

    /**
     * Method to set the meeting date
     */
    fun setMeetingScheduleDate(calendar: Calendar) {
        this.meetingStartTime.calendar = calendar
        this.meetingEndTime.calendar = calendar
    }

    /**
     * method to set the meeting time
     */
    fun setMeetingTime(time: MeetingTime) {
        time.isInitialized = true
        when (time) {
            MEETING_START_TIME -> meetingStartTime = time
            MEETING_END_TIME -> meetingEndTime = time
        }
    }

    /**
     * Method used to schedule the meeting
     */
    fun updateMeeting(description: String) {
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingStartTime.calendar)
        val observer = Observer<List<MeetingDataModel>> {
            if (!isTimeAvailable(it)) {
                meetingScheduledConfirmation.value = false
            } else {
                dataBaseQuery.insertMeetingSchedule(
                    MeetingDataModel(
                        meetingStartTime.calendar,
                        meetingEndTime.calendar,
                        description
                    )
                )
                meetingScheduledConfirmation.value = true
            }
        }
        // Reset the live data value, so that observeOnceAndNonNull observe new non null value.
        meetingScheduleLiveData.value = null
        meetingScheduleLiveData.observeOnceAndNonNull(observer)
    }


    private fun isTimeAvailable(meetingDataModel: List<MeetingDataModel>): Boolean {
        var isTimeAvailable = true
        meetingDataModel.forEach {
            if (it.isTimeOverlapWith(meetingStartTime) || it.isTimeOverlapWith(meetingEndTime)) {
                //timeslot overlapped with the requested time
                isTimeAvailable = false
            }
        }
        return isTimeAvailable
    }

    private fun getDateWithOffset(day: Int) {
        meetingStartTime.calendar.add(Calendar.DATE, day)
        meetingEndTime.calendar.add(Calendar.DATE, day)
    }
}
