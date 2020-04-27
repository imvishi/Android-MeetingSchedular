package com.example.meetingscheduler.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import java.util.*

/**
 * manager class to manage date and time picker
 */
object DateTimePickerManager {

    val dateUpdatedLiveData = MutableLiveData<Calendar>()
    val timeUpdatedLiveData = MutableLiveData<Calendar>()

    /**
     * Method shows the DatePickerView to Select the date
     */
    fun showDatePicker(calendar: Calendar, context: Context) {
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
     * Method shows the TimePickerView to Select the time
     */
    fun showTimePicker(calendar: Calendar, context: Context) {
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.apply {
                set(Calendar.HOUR, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            timeUpdatedLiveData.value = calendar
        }
        TimePickerDialog(
            context,
            listener,
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}