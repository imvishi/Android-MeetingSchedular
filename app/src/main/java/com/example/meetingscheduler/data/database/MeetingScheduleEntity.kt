package com.example.meetingscheduler.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meeting_schedule_table")
data class MeetingScheduleEntity(
    @PrimaryKey(autoGenerate = true) var meetingId: Int = 0,
    @ColumnInfo(name = "meetingDate") var meetingDate: Int,
    @ColumnInfo(name = "startTime") var startTimeInMinutes: Int,
    @ColumnInfo(name = "endTime") var endTimeInMinutes: Int,
    @ColumnInfo(name = "description") var description: String
)