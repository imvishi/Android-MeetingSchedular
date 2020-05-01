package com.example.meetingscheduler.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meeting_schedule_table")
data class MeetingScheduleEntity(
    @PrimaryKey(autoGenerate = true) var meetingId: Int = 0,
    var meetingDate: String,
    @ColumnInfo(name = "startTime") var startTimeInMillis: Long,
    @ColumnInfo(name = "endTime") var endTimeInMillis: Long,
    var description: String
)