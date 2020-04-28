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
)