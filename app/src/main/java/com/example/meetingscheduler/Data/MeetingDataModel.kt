package com.example.meetingscheduler.Data

/**
 * data model for meetings
 */
data class MeetingDataModel(
    val startTime: String,
    val endTime: String,
    val description: String,
    val participants: String
)