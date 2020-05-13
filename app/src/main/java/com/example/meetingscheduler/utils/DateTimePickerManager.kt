package com.example.meetingscheduler.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.*

/**
 * Manager class to manage date and time picker
 */
class DateTimePickerManager(val context: Context) {

    /**
     * Method shows the DatePickerView to select the date
     */
    fun showDatePicker( onDatePicked: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance()
        val listener = DatePickerDialog.OnDateSetListener { v, year, month, dayOfMonth ->
            calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            onDatePicked.invoke(calendar)
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
    fun showTimePicker(onTimePicked: (Int) -> Unit) {
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            onTimePicked.invoke(hourOfDay * 60 + minute)
        }
        TimePickerDialog(context, listener, 0, 0, true).show()
    }
}