package com.example.meetingscheduler.data

import java.util.*

/**
 * data model for meetings
 */
data class MeetingDataModel(
    val startTime: Calendar,
    val endTime: Calendar,
    val description: String
) {

    /**
     * method to check is meeting time is overlap with [startTime] and [endTime]
     */
    fun isTimeOverlapWith(meetingTime: MeetingTime): Boolean {
        val meetingTimeInMinutes = meetingTime.calendar.timeInMillis
        val startTimeInMinutes = startTime.timeInMillis
        val endTimeInMinutes = endTime.timeInMillis
        if (meetingTimeInMinutes in startTimeInMinutes..endTimeInMinutes) {
            // requested time clashed with scheduled meeting time
            return true
        }
        return false
    }
}