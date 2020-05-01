package com.example.meetingscheduler.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd-MM-YYYY"
private const val TIME_FORMAT = "HH:mm"

/**
 * Extension function to check if the given calendar date has already passed
 */
fun Calendar.hasDateAlreadyPassed(): Boolean {
    if (DateUtils.isToday(this.timeInMillis)) {
        return false
    }
    return this.time < Calendar.getInstance().time
}

/**
 * Extension function to get the calendar date in [DATE_FORMAT]
 */
fun Calendar.getDateInDateFormat(): String {
    val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(this.time)
}

/**
 * Extension function to get the calendar time in [TIME_FORMAT]
 */
fun Calendar.getTimeInTimeFormat(): String {
    val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
    return timeFormat.format(this.time)
}