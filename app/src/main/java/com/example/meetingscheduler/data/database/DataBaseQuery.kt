package com.example.meetingscheduler.data.database

import android.content.Context
import android.util.Log
import com.example.meetingscheduler.data.MeetingScheduleDataModel
import com.example.meetingscheduler.utils.getDateWithOutTime
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "DataBaseQuery"
/**
 * Abstract layer between database and the code.
 */
class DataBaseQuery(context: Context) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val dataBase = MeetingScheduleDataBase.getDatabase(context)

    /**
     * method to insert the meeting schedule into the data base
     */
    fun insertMeetingSchedule(
        meetingSchedule: MeetingScheduleDataModel,
        onInserted: () -> Unit
    ) {
        coroutineScope.launch {
            val addedId = async {
                dataBase
                    .meetingScheduleDao()
                    .insertMeetingSchedule(
                        MeetingScheduleEntity(
                            meetingDate = meetingSchedule.meetingDate,
                            startTimeInMinutes = meetingSchedule.startTimeInMinutes,
                            endTimeInMinutes = meetingSchedule.endTimeInMinutes,
                            description = meetingSchedule.description
                        )
                    )
            }.await()
            withContext(Dispatchers.Main) {
                Log.d(TAG, "Value inserted successfully with id $addedId")
                onInserted.invoke()
            }
        }
    }

    /**
     * method used to get the list of all meeting schedule at given date
     */
    fun selectAllMeetingScheduleAtDate(
        meetingDate: Calendar,
        onFetched: (List<MeetingScheduleDataModel>) -> Unit
    ) {
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
                        it.startTimeInMinutes,
                        it.endTimeInMinutes,
                        it.description
                    )
                }
                onFetched.invoke(meetings)
            }
        }
    }

    /**
     * method used to get the count of meeting schedules that are overlapped with the given time
     */
    fun selectCountOfOverlappedMeetingWithGivenTime(
        meetingDate: Calendar,
        meetingStartTimeInMinutes: Int,
        meetingEndTimeInMinutes: Int,
        onFetched: (Int) -> Unit
    ) {
        coroutineScope.launch {
            val meetingScheduleCount = async {
                dataBase.meetingScheduleDao().getMeetingsOverlappedWithRequestedTime(
                    meetingDate.getDateWithOutTime().time,
                    meetingStartTimeInMinutes,
                    meetingEndTimeInMinutes
                )
            }.await()
            withContext(Dispatchers.Main) {
                onFetched.invoke(meetingScheduleCount)
            }
        }
    }
}