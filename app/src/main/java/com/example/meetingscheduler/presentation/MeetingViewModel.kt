package com.example.meetingscheduler.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meetingscheduler.data.*
import com.example.meetingscheduler.data.database.DataBaseQuery
import java.util.*

class MeetingViewModel(val app: Application) : AndroidViewModel(app) {

    private val dataBaseQuery = DataBaseQuery(app.applicationContext)

    val meetingScheduledConfirmation = MutableLiveData<Boolean>()
    val meetingScheduleLiveData = MutableLiveData<List<MeetingScheduleDataModel>>()

    var meetingDateCalendar = Calendar.getInstance()
    var meetingStartTimeInMinutes = -1
    var meetingEndTimeInMinutes = -1

    /**
     * method used to fetch the current day schedule
     */
    fun getCurrentDaySchedule() {
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingDateCalendar, ::onFetched)
    }

    /**
     * method used to fetch next day schedule
     */
    fun getNextDaySchedule() {
        getDateWithOffset(1)
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingDateCalendar, ::onFetched)
    }

    /**
     * method used to fetch prev day schedule
     */
    fun getPrevDaySchedule() {
        getDateWithOffset(-1)
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingDateCalendar, ::onFetched)
    }

    /**
     * Method used to schedule the meeting
     */
    fun updateMeeting(description: String) {
        dataBaseQuery.selectCountOfOverlappedMeetingWithGivenTime(
            meetingDateCalendar,
            meetingStartTimeInMinutes,
            meetingEndTimeInMinutes
        ) {
            if (it == 0) {
                // Schedule meeting if previous meetings do not overlapped with the requested time
                val meetingSchedule = MeetingScheduleDataModel(
                    meetingDateCalendar.time,
                    meetingStartTimeInMinutes,
                    meetingEndTimeInMinutes,
                    description.trim()
                )
                dataBaseQuery.insertMeetingSchedule(meetingSchedule) {
                    meetingScheduledConfirmation.value = true
                }
            } else {
                meetingScheduledConfirmation.value = false
            }
        }
    }

    private fun onFetched(meetingScheduleDataModels: List<MeetingScheduleDataModel>) {
        meetingScheduleLiveData.value = meetingScheduleDataModels
    }

    private fun getDateWithOffset(day: Int) {
        meetingDateCalendar.add(Calendar.DATE, day)
    }
}
