package com.example.meetingscheduler.data

import android.content.Context
import com.example.meetingscheduler.R
import com.example.meetingscheduler.utils.TimeUtils.getTimeFromTimeInMillis

/**
 * Representation of meeting start and end Time
 */
enum class MeetingTime {

    MEETING_START_TIME {
        override fun getTimeToShow(context: Context): CharSequence {
            return if (isInitialized) {
                getTimeFromTimeInMillis(timeInMillis)
            } else {
                context.resources.getText(R.string.start_time_prompt)
            }
        }
    },
    MEETING_END_TIME {
        override fun getTimeToShow(context: Context): CharSequence {
            return if (isInitialized) {
                getTimeFromTimeInMillis(timeInMillis)
            } else {
                context.resources.getText(R.string.end_time_prompt)
            }
        }
    };

    var timeInMillis = 0L
    var isInitialized = false
    abstract fun getTimeToShow(context: Context): CharSequence
}