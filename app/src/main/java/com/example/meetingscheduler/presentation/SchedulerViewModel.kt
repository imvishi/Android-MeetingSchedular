package com.example.meetingscheduler.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.meetingscheduler.data.*
import com.example.meetingscheduler.data.MeetingTime.MEETING_END_TIME
import com.example.meetingscheduler.data.MeetingTime.MEETING_START_TIME
import com.example.meetingscheduler.data.database.DataBaseQuery
import com.example.meetingscheduler.data.database.DataBaseQuery.Callback
import com.example.meetingscheduler.utils.getDateWithOutTime
import java.util.*

/**
 * max description length
 */
private const val MAX_DESCRIPTION_LENGTH = 140

class SchedulerViewModel(val app: Application) : AndroidViewModel(app), Callback {

    private val dataBaseQuery = DataBaseQuery(app.applicationContext, this)
    private lateinit var meetingSchedule: MeetingScheduleDataModel

    val meetingScheduledConfirmation = MutableLiveData<Boolean>()
    var meetingDateCalendar = Calendar.getInstance()
    var meetingStartTime = MEETING_START_TIME
    var meetingEndTime = MEETING_END_TIME

    override fun onMeetingScheduleFetched(meetingSchedules: List<MeetingScheduleDataModel>) {
        if (isTimeAvailable(meetingSchedules)) {
            dataBaseQuery.insertMeetingSchedule(meetingSchedule)
            meetingScheduledConfirmation.value = true
        } else {
            meetingScheduledConfirmation.value = false
        }
    }

    /**
     * Method to set the meeting date
     */
    fun setMeetingScheduleDate(calendar: Calendar) {
        meetingDateCalendar = calendar.getDateWithOutTime()
        meetingStartTime.apply {
            timeInMillis = meetingDateCalendar.timeInMillis
            isInitialized = false
        }
        meetingEndTime.apply {
            timeInMillis = meetingDateCalendar.timeInMillis
            isInitialized = false
        }
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
        val descriptionLength = minOf(description.length, MAX_DESCRIPTION_LENGTH)
        meetingSchedule = MeetingScheduleDataModel(
            meetingDateCalendar.time,
            meetingStartTime.timeInMillis,
            meetingEndTime.timeInMillis,
            description.substring(0, descriptionLength)
        )
        dataBaseQuery.selectAllMeetingScheduleAtDate(meetingDateCalendar)
    }


    private fun isTimeAvailable(meetingSchedules: List<MeetingScheduleDataModel>): Boolean {
        meetingSchedules.forEach {
            if (it.isTimeOverlapWith(meetingStartTime) || it.isTimeOverlapWith(meetingEndTime)) {
                //timeslot overlapped with the requested time
                return false
            }
        }
        return true
    }
}
