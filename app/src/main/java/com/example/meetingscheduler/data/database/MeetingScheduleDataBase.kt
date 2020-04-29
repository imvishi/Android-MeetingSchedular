package com.example.meetingscheduler.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "meeting_database.db"

@Database(entities = [MeetingScheduleEntity::class], version = 1)
abstract class MeetingScheduleDataBase : RoomDatabase() {
    abstract fun meetingScheduleDao(): MeetingScheduleDao

    companion object {
        @Volatile private var INSTANCE: MeetingScheduleDataBase? = null

        fun getDatabase(context: Context): MeetingScheduleDataBase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = buildDatabase(context.applicationContext)
                INSTANCE = instance
                return instance
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            MeetingScheduleDataBase::class.java,
            DATABASE_NAME
        ).build()
    }
}