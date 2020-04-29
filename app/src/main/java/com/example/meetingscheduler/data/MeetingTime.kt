package com.example.meetingscheduler.data

/**
 * Data representation of meeting time
 */
sealed class MeetingTime(open var hours: Int, open var minutes: Int) {

    /**
     * function to format the time  to add the leading zero
     */
    fun formatTime() = String.format("%02d:%02d", this.hours, this.minutes)

    /**
     * function to check is meeting time is initialized or not
     */
    fun isInitialized() = (this.hours >= 0 && this.minutes >= 0)

    /**
     * function to compare two times
     */
    fun isGreaterThan(time: MeetingTime): Boolean {
        if (time.hours > this.hours) {
            return false
        }
        if (this.hours == time.hours) {
            return this.minutes > time.minutes
        }
        return this.minutes >= time.minutes
    }

    /**
     * method used to get time into minutes
     */
    fun getTimeInMinutes()  = this.hours * 60 + this.minutes

    /**
     * method used to reset the meeting time
     */
    fun resetMeetingTime() {
        hours = -1
        minutes = -1
    }
}

/**
 * Data representation of meeting start time
 */
data class StartTime(
    override var hours: Int,
    override var minutes: Int
) : MeetingTime(hours, minutes)

/**
 * Data representation of meeting end time
 */
data class EndTime(
    override var hours: Int,
    override var minutes: Int
) : MeetingTime(hours, minutes)
