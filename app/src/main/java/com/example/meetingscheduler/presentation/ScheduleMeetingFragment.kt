package com.example.meetingscheduler.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.meetingscheduler.data.MeetingTime
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
            viewModel.meetingStartTime.isInitialized = false
            viewModel.meetingEndTime.isInitialized = false
        }
    }

    private fun onDateUpdated(calendar: Calendar) {
        viewModel.setMeetingScheduleDate(calendar)
        updateDateView()
        updateTimeView()
    }

    private fun onTimeUpdated(meetingTime: MeetingTime) {
        viewModel.setMeetingTime(meetingTime)
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
            dateTimePickerManager.showDatePicker(viewModel.meetingStartTime.calendar)
        }
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
        datePickerView.text = viewModel.meetingStartTime.calendar.getDateInDateFormat()
        enableScheduleMeetingButton()
    }

    private fun updateTimeView() {
        if (viewModel.meetingStartTime.isInitialized) {
            startTimePickerView.text = viewModel.meetingStartTime.calendar.getTimeInTimeFormat()
        } else {
            startTimePickerView.text = resources.getText(R.string.start_time_prompt)
        }

        if (viewModel.meetingEndTime.isInitialized) {
            endTimePickerView.text = viewModel.meetingEndTime.calendar.getTimeInTimeFormat()
        } else {
            endTimePickerView.text = resources.getText(R.string.end_time_prompt)
        }
        enableScheduleMeetingButton()
    }

    /**
     * enable schedule meeting button only when endtime is greater than start time
     *  and description text is not empty
     */
    private fun enableScheduleMeetingButton() {
        if (viewModel.meetingEndTime.calendar.time > viewModel.meetingStartTime.calendar.time
            && descriptionText.text.toString().isNotEmpty()
            && !viewModel.meetingStartTime.calendar.hasDateAlreadyPassed()
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