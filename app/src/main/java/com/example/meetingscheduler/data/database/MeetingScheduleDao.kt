package com.example.meetingscheduler.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao interface MeetingScheduleDao {

    @Query("SELECT * FROM meeting_schedule_table WHERE meetingDate = :date")
    fun getMeetingSchedule(date: String): List<MeetingScheduleEntity>

    @Insert
    fun insertMeetingSchedule(meeting: MeetingScheduleEntity)
}