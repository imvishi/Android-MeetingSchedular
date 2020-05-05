package com.example.meetingscheduler.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private const val TIME_FORMAT = "HH:mm"
    /**
     * Utility function to get the time in [TIME_FORMAT]
     */
    fun getTimeFromTimeInMillis(timeInMillis: Long): String {
        val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        val calendar = Calendar.getInstance().apply { this.timeInMillis = timeInMillis }
        return timeFormat.format(calendar.time)
    }
}