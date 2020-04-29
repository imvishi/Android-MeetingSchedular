package com.example.meetingscheduler.data.database

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromTimeStampToDate(timeInMillis: Long): Date {
        return Date(timeInMillis)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

}