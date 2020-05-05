package com.example.meetingscheduler.data.database

import android.content.Context
import com.example.meetingscheduler.data.MeetingScheduleDataModel
import com.example.meetingscheduler.utils.getDateWithOutTime
import kotlinx.coroutines.*
import java.util.*

/**
 * Abstract layer between database and the code.
 */
class DataBaseQuery(context: Context, val listener: Callback) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val dataBase = MeetingScheduleDataBase.getDatabase(context)


    interface Callback {
        /**
         * method will be called on meeting scheduled fetched from server
         */
        fun onMeetingScheduleFetched(meetingSchedules: List<MeetingScheduleDataModel>)
    }

    /**
     * method to insert the meeting schedule into the data base
     */
    fun insertMeetingSchedule(meetingSchedule: MeetingScheduleDataModel) {
        coroutineScope.launch {
            dataBase
                .meetingScheduleDao()
                .insertMeetingSchedule(
                    MeetingScheduleEntity(
                        meetingDate = meetingSchedule.meetingDate,
                        startTimeInMillis = meetingSchedule.startTimeInMillis,
                        endTimeInMillis = meetingSchedule.endTimeInMillis,
                        description = meetingSchedule.description
                    )
                )
        }
    }
    /**
     * method used to get the list of all meeting schedule at given date
     */
    fun selectAllMeetingScheduleAtDate(meetingDate: Calendar) {
        coroutineScope.launch {
            val meetingSchedule = async {
                dataBase
                    .meetingScheduleDao()
                    .getMeetingSchedule(meetingDate.getDateWithOutTime().time)
            }.await()
            withContext(Dispatchers.Main) {
                val meetings = meetingSchedule.map {
                    MeetingScheduleDataModel(
                        it.meetingDate,
                        it.startTimeInMillis,
                        it.endTimeInMillis,
                        it.description
                    )
                }
                listener.onMeetingScheduleFetched(meetings)
            }
        }
    }
}