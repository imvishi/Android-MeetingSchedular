package com.example.meetingscheduler.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meetingscheduler.data.MeetingTime
import com.example.meetingscheduler.R
import com.example.meetingscheduler.data.MeetingTime.MEETING_END_TIME
import com.example.meetingscheduler.data.MeetingTime.MEETING_START_TIME
import com.example.meetingscheduler.utils.*
import kotlinx.android.synthetic.main.meeting_schedule_fragment.*
import java.util.*

/**
 * Fragment used to schedule the meeting
 */
class ScheduleMeetingFragment : Fragment() {

    companion object {
        const val MEETING_DATE = "meeting_date"
        fun newInstance(calendar: Calendar) = ScheduleMeetingFragment().apply {
            this.arguments = bundleOf(Pair(MEETING_DATE, calendar))
        }
    }

    private lateinit var viewModel: SchedulerViewModel
    private lateinit var dateTimePickerManager: DateTimePickerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SchedulerViewModel::class.java).also {
            it.meetingScheduledConfirmation.observeNonNull(this, Observer(::showAlertMessage))
        }
        dateTimePickerManager = DateTimePickerManager(requireContext()).also {
            it.dateUpdatedLiveData.observe(this, Observer(::onDateUpdated))
            it.timeUpdatedLiveData.observe(this, Observer(::onTimeUpdated))
        }
        if (savedInstanceState == null) {
            val calendar = arguments!!.getSerializable(MEETING_DATE) as Calendar
            viewModel.setMeetingScheduleDate(calendar)
        }
    }

    /**
     * method to show the alert message
     */
    private fun showAlertMessage(meetingScheduled: Boolean) {
        loadingProgressBar.visibility = View.GONE
        val messageToShow = when (meetingScheduled) {
            true -> "Meeting Scheduled successfully."
            false -> "Please select another slot."
        }
        AlertDialog.Builder(requireContext()).setMessage(messageToShow).show()
        viewModel.meetingScheduledConfirmation.value = null
    }

    private fun onDateUpdated(calendar: Calendar) {
        viewModel.setMeetingScheduleDate(calendar)
        updateDateView()
        updateStartTimeView()
        updateEndTimeView()
    }

    private fun onTimeUpdated(meetingTime: MeetingTime) {
        viewModel.setMeetingTime(meetingTime)
        when (meetingTime) {
            MEETING_START_TIME -> updateStartTimeView()
            MEETING_END_TIME -> updateEndTimeView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.meeting_schedule_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDateView()
        updateStartTimeView()
        updateEndTimeView()
        back.setOnClickListener { requireActivity().onBackPressed() }
        datePickerView.setOnClickListener { dateTimePickerManager.showDatePicker() }
        startTimePickerView.setOnClickListener {
            dateTimePickerManager.showTimePicker(viewModel.meetingStartTime)
        }
        endTimePickerView.setOnClickListener {
            dateTimePickerManager.showTimePicker(viewModel.meetingEndTime)
        }
        descriptionText.doOnTextChanged { _, _, _, _ ->
            enableScheduleMeetingButton()
        }
        scheduleMeetingButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            viewModel.updateMeeting(descriptionText.text.toString())
        }
    }

    private fun updateDateView() {
        datePickerView.text = viewModel.meetingDateCalendar.getDateInDateFormat()
        enableScheduleMeetingButton()
    }

    private fun updateStartTimeView() {
        startTimePickerView.text = viewModel.meetingStartTime.getTimeToShow(requireContext())
        enableScheduleMeetingButton()
    }

    private fun updateEndTimeView() {
        endTimePickerView.text = viewModel.meetingEndTime.getTimeToShow(requireContext())
        enableScheduleMeetingButton()
    }

    /**
     * enable schedule meeting button only when endtime is greater than start time
     *  and description text is not empty
     */
    private fun enableScheduleMeetingButton() {
        val enableButton =
            viewModel.meetingEndTime.timeInMillis > viewModel.meetingStartTime.timeInMillis
                    && descriptionText.text.toString().isNotBlank()
                    && !viewModel.meetingDateCalendar.hasDateAlreadyPassed()
        scheduleMeetingButton.disableButton(!enableButton)
    }
}