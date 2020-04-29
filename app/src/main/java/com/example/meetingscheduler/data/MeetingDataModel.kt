package com.example.meetingscheduler.data

import java.util.*

/**
 * data model for meetings
 */
data class MeetingDataModel(
    val meetingDate: Calendar,
    val startTime: StartTime,
    val endTime: EndTime,
    val description: String
) {

    /**
     * method to check is meeting time is overlap with [startTime] and [endTime]
     */
    fun isTimeOverlapWith(meetingTime: MeetingTime): Boolean {
        val meetingTimeInMinutes = meetingTime.getTimeInMinutes()
        val startTimeInMinutes = startTime.getTimeInMinutes()
        val endTimeInMinutes = endTime.getTimeInMinutes()
        if (meetingTimeInMinutes in startTimeInMinutes..endTimeInMinutes) {
            // requested time clashed with scheduled meeting time
            return true
        }
        return false
    }
}