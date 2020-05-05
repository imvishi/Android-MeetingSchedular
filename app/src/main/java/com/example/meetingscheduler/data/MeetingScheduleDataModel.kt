package com.example.meetingscheduler.data

import java.util.*

/**
 * data model for meetings
 */
data class MeetingScheduleDataModel(
    val meetingDate: Date,
    val startTimeInMillis: Long,
    val endTimeInMillis: Long,
    val description: String
) {

    /**
     * method to check is meeting time is overlap with [startTime] and [endTime]
     */
    fun isTimeOverlapWith(meetingTime: MeetingTime): Boolean {
        if (meetingTime.timeInMillis in startTimeInMillis..endTimeInMillis) {
            // requested time clashed with scheduled meeting time
            return true
        }
        return false
    }
}