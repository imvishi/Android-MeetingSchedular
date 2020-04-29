package com.example.meetingscheduler.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meetingscheduler.data.EndTime
import com.example.meetingscheduler.data.MeetingTime
import com.example.meetingscheduler.data.StartTime
import com.example.meetingscheduler.R
import com.example.meetingscheduler.utils.*
import kotlinx.android.synthetic.main.meeting_schedule_fragment.*
import java.util.*

/**
 * Fragment used to schedule the meeting
 */
class ScheduleMeetingFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleMeetingFragment()
    }

    private lateinit var viewModel: SchedulerViewModel
    private lateinit var dateTimePickerManager: DateTimePickerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SchedulerViewModel::class.java).also {
            it.meetingScheduledConfirmation.observeNonNull(this, Observer {
                loadingProgressBar.visibility = View.GONE
                if (it) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Meeting Scheduled successfully.")
                        .show()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Please select another slot.")
                        .show()
                }
                viewModel.meetingScheduledConfirmation.value = null
            })

        }
        dateTimePickerManager = DateTimePickerManager(requireContext()).also {
            it.dateUpdatedLiveData.observe(this, Observer(::onDateUpdated))
            it.timeUpdatedLiveData.observe(this, Observer(::onTimeUpdated))
        }

        if (savedInstanceState == null) {
            viewModel.calendarEndTime.resetMeetingTime()
            viewModel.calendarStartTime.resetMeetingTime()
        }
    }

    private fun onDateUpdated(calendar: Calendar) {
        viewModel.setMeetingScheduleDate(calendar)
        updateDateView()
    }

    private fun onTimeUpdated(time: MeetingTime) {
        viewModel.setMeetingTime(time)
        updateTimeView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.meeting_schedule_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDateView()
        updateTimeView()
        back.setOnClickListener { requireActivity().onBackPressed() }
        datePickerView.setOnClickListener {
            dateTimePickerManager.showDatePicker(viewModel.calendar)
        }
        startTimePickerView.setOnClickListener {
            dateTimePickerManager.showTimePicker(StartTime(0, 0))
        }
        endTimePickerView.setOnClickListener {
            dateTimePickerManager.showTimePicker(EndTime(0, 0))
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
        datePickerView.text = viewModel.calendar.getDateInDateFormat()
        enableScheduleMeetingButton()
    }

    private fun updateTimeView() {
        if (viewModel.calendarStartTime.isInitialized()) {
            startTimePickerView.text = viewModel.calendarStartTime.formatTime()
        }

        if (viewModel.calendarEndTime.isInitialized()) {
            endTimePickerView.text = viewModel.calendarEndTime.formatTime()
        }
        enableScheduleMeetingButton()
    }

    /**
     * enable schedule meeting button only when endtime is greater than start time
     *  and description text is not empty
     */
    private fun enableScheduleMeetingButton() {
        if (viewModel.calendarEndTime.isGreaterThan(viewModel.calendarStartTime)
            && descriptionText.text.toString().isNotEmpty()
            && !viewModel.calendar.hasDateAlreadyPassed()
        ) {
            scheduleMeetingButton.apply {
                alpha = 1.0f
                isEnabled = true
            }
        } else {
            scheduleMeetingButton.apply {
                alpha = 0.2f
                isEnabled = false
            }
        }
    }
}