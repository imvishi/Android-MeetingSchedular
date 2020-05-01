package com.example.meetingscheduler.data

import java.util.*

/**
 * Representation of meeting start and end Time
 */
enum class MeetingTime {

    MEETING_START_TIME {
        override var calendar = Calendar.getInstance()
        override var isInitialized = false
    },
    MEETING_END_TIME {
        override var calendar: Calendar = Calendar.getInstance()
        override var isInitialized = false
    };

    abstract var calendar: Calendar
    abstract var isInitialized: Boolean
}