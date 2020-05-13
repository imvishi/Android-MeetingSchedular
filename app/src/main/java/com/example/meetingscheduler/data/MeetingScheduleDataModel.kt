package com.example.meetingscheduler.data

import java.util.*

/**
 * data model for meetings
 */
data class MeetingScheduleDataModel(
    val meetingDate: Date,
    val startTimeInMinutes: Int,
    val endTimeInMinutes: Int,
    val description: String
)