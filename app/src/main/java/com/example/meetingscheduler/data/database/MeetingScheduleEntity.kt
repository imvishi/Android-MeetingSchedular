package com.example.meetingscheduler.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "meeting_schedule_table")
data class MeetingScheduleEntity(
    @PrimaryKey(autoGenerate = true) var meetingId: Int = 0,
    var meetingDate: Date,
    @ColumnInfo(name = "startTime") var startTimeInMinutes: Int,
    @ColumnInfo(name = "endTime") var endTimeInMinutes: Int,
    var description: String
)