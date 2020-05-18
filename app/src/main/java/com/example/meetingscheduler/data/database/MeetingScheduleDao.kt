package com.example.meetingscheduler.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao interface MeetingScheduleDao {

    @Query("SELECT * FROM meeting_schedule_table WHERE meetingDate = :date ORDER BY startTime")
    fun getMeetingSchedule(date: Date): List<MeetingScheduleEntity>

    @Insert
    fun insertMeetingSchedule(meeting: MeetingScheduleEntity): Long

    @Query(
        "SELECT Count(*) FROM meeting_schedule_table WHERE " +
                "meetingDate = :date " +
                "AND ((:meetingStartTimeInMinutes >= startTime AND :meetingStartTimeInMinutes < endTime) " +
                "OR (:meetingEndTimeInMinutes > startTime AND :meetingEndTimeInMinutes <= endTime))" +
                "OR ((startTime > :meetingStartTimeInMinutes AND startTime < :meetingEndTimeInMinutes) " +
                "AND (endTime > :meetingStartTimeInMinutes AND endTime < :meetingEndTimeInMinutes))"
    )
    fun getMeetingsOverlappedWithRequestedTime(
        date: Date,
        meetingStartTimeInMinutes: Int,
        meetingEndTimeInMinutes: Int
    ): Int
}