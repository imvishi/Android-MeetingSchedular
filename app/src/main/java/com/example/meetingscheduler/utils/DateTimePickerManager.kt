package com.example.meetingscheduler.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.meetingscheduler.data.MeetingTime
import java.util.*

/**
 * Manager class to manage date and time picker
 */
class DateTimePickerManager(val context: Context) {

    val dateUpdatedLiveData = MutableLiveData<Calendar>()
    val timeUpdatedLiveData = MutableLiveData<MeetingTime>()

    /**
     * Method shows the DatePickerView to select the date
     */
    fun showDatePicker(calendar: Calendar) {
        val listener = DatePickerDialog.OnDateSetListener { v, year, month, dayOfMonth ->
            calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            dateUpdatedLiveData.value = calendar
        }
        DatePickerDialog(
            context, listener,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    /**
     * Method shows the TimePickerView to select the time
     */
    fun showTimePicker(meetingTime: MeetingTime) {
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            meetingTime.calendar.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            timeUpdatedLiveData.value = meetingTime
        }
        TimePickerDialog(context, listener, 0, 0, true).show()
    }
}