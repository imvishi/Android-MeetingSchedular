package com.example.meetingscheduler.utils

import java.util.*

object CalendarUtils {

    /**
     * method returns new calendar without time section
     */
    fun getCalendarWithoutTime(calendar: Calendar): Calendar {
        val newCalendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return newCalendar
    }
}