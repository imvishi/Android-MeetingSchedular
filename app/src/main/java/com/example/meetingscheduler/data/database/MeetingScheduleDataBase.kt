package com.example.meetingscheduler.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE_NAME = "meeting_database.db"

@Database(entities = [MeetingScheduleEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class MeetingScheduleDataBase : RoomDatabase() {
    abstract fun meetingScheduleDao(): MeetingScheduleDao

    companion object {
        @Volatile private var instance: MeetingScheduleDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            MeetingScheduleDataBase::class.java,
            DATABASE_NAME
        ).build()
    }
}