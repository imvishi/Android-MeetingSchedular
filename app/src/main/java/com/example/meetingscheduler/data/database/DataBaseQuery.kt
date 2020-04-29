package com.example.meetingscheduler.data.database

import android.content.Context
import com.example.meetingscheduler.data.EndTime
import com.example.meetingscheduler.data.MeetingDataModel
import com.example.meetingscheduler.data.StartTime
import com.example.meetingscheduler.utils.getTotalNumberOfDays
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
        fun onMeetingScheduleFetched(meetingDataModel: List<MeetingDataModel>)
    }

    /**
     * method to insert the meeting schedule into the data base
     */
    fun insertMeetingSchedule(meetingSchedule: MeetingDataModel) {
        coroutineScope.launch {
            dataBase
                .meetingScheduleDao()
                .insertMeetingSchedule(
                    MeetingScheduleEntity(
                        meetingDate = meetingSchedule.meetingDate.getTotalNumberOfDays(),
                        startTimeInMinutes = meetingSchedule.startTime.getTimeInMinutes(),
                        endTimeInMinutes = meetingSchedule.endTime.getTimeInMinutes(),
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
                dataBase.meetingScheduleDao().getMeetingSchedule(meetingDate.getTotalNumberOfDays())
            }.await()
            withContext(Dispatchers.Main) {
                val meetings = meetingSchedule.map {
                    MeetingDataModel(
                        Calendar.getInstance().apply { it.meetingDate*60*60*24*1000 },
                        StartTime(it.startTimeInMinutes / 60, it.startTimeInMinutes % 60),
                        EndTime(it.endTimeInMinutes / 60, it.endTimeInMinutes % 60),
                        it.description
                    )
                }
                listener.onMeetingScheduleFetched(meetings)
            }
        }
    }
}