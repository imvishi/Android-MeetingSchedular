package com.example.meetingscheduler.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.meetingscheduler.data.*
import com.example.meetingscheduler.data.database.DataBaseQuery
import com.example.meetingscheduler.data.database.DataBaseQuery.Callback
import com.example.meetingscheduler.utils.observeOnce
import java.util.*

class SchedulerViewModel(val app: Application) : AndroidViewModel(app), Callback {

    private val dataBaseQuery = DataBaseQuery(app.applicationContext, this)
    val meetingScheduleLiveData = MutableLiveData<List<MeetingDataModel>>()
    val meetingScheduledConfirmation = MutableLiveData<Boolean>()
    var calendar: Calendar = Calendar.getInstance()
    var calendarStartTime = StartTime(-1,-1)
    var calendarEndTime = EndTime(-1,-1)


    override fun onMeetingScheduleFetched(meetingDataModel: List<MeetingDataModel>) {
        meetingScheduleLiveData.value = meetingDataModel
    }

    /**
     * method to fetch current day schedule
     */
    fun getCurrentDaySchedule() {
        getDateWithOffset(0)
        dataBaseQuery.selectAllMeetingScheduleAtDate(calendar)
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
        dataBaseQuery.selectAllMeetingScheduleAtDate(calendar)
        val observers = Observer<List<MeetingDataModel>> {
            val isTimeAvailable = isTimeAvailable(it)
            if (!isTimeAvailable) {
                meetingScheduledConfirmation.value = false
            } else {
                meetingScheduledConfirmation.value = true
                dataBaseQuery.insertMeetingSchedule(
                    MeetingDataModel(
                        calendar,
                        calendarStartTime,
                        calendarEndTime,
                        description
                    )
                )
            }
        }
        meetingScheduleLiveData.observeOnce(observers)
    }


    private fun isTimeAvailable(meetingDataModel: List<MeetingDataModel>): Boolean {
        var isTimeAvailable = true
        meetingDataModel.forEach {
            if (it.isTimeOverlapWith(calendarStartTime) || it.isTimeOverlapWith(calendarEndTime)) {
                //timeslot overlapped with the requested time
                isTimeAvailable = false
            }
        }
        return isTimeAvailable
    }

    private fun getDateWithOffset(day: Int) {
        calendar.add(Calendar.DATE, day)
    }
}
