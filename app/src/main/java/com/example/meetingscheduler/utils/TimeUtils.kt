package com.example.meetingscheduler.utils

object TimeUtils {

    /**
     * Utility function to get the time in [TIME_FORMAT]
     */
    fun getTimeFromTimeInMinutes(timeInMinutes: Int): String {
        val hours = String.format("%02d", timeInMinutes / 60)
        val minutes = String.format("%02d", timeInMinutes % 60)
        return "$hours:$minutes"
    }
}