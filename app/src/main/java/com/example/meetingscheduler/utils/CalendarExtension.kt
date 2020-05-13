package com.example.meetingscheduler.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd-MM-YYYY"

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
 * Extension function to get the calendar date without time
 */
fun Calendar.getDateWithOutTime() = this.apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}