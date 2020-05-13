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
import com.example.meetingscheduler.R
import com.example.meetingscheduler.utils.*
import com.example.meetingscheduler.utils.TimeUtils.getTimeFromTimeInMinutes
import kotlinx.android.synthetic.main.meeting_schedule_fragment.*

/**
 * Fragment used to schedule the meeting
 */
class ScheduleMeetingFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleMeetingFragment()
    }

    private lateinit var viewModel: MeetingViewModel
    private lateinit var dateTimePickerManager: DateTimePickerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MeetingViewModel::class.java).also {
            it.meetingScheduledConfirmation.observeNonNull(this, Observer(::showAlertMessage))
        }
        dateTimePickerManager = DateTimePickerManager(requireContext())

        if (savedInstanceState == null) {
            viewModel.meetingEndTimeInMinutes = -1
            viewModel.meetingStartTimeInMinutes = -1
        }
    }

    /**
     * method to show the alert message
     */
    private fun showAlertMessage(meetingScheduled: Boolean) {
        pb_loading.visibility = View.GONE
        val messageToShow = when (meetingScheduled) {
            true -> "Meeting Scheduled successfully."
            false -> "Please select another slot."
        }
        AlertDialog.Builder(requireContext()).setMessage(messageToShow).show()
        viewModel.meetingScheduledConfirmation.value = null
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
        tv_date_picker.setOnClickListener {
            dateTimePickerManager.showDatePicker {
                viewModel.meetingDateCalendar = it
                updateDateView()
            }
        }
        tv_start_time_picker.setOnClickListener {
            dateTimePickerManager.showTimePicker {
                viewModel.meetingStartTimeInMinutes = it
                updateStartTimeView()
            }
        }
        tv_end_time_picker.setOnClickListener {
            dateTimePickerManager.showTimePicker {
                viewModel.meetingEndTimeInMinutes = it
                updateEndTimeView()
            }
        }
        tv_description_text.doOnTextChanged { _, _, _, _ ->
            enableScheduleMeetingButton()
        }
        bt_schedule_meeting.setOnClickListener {
            pb_loading.visibility = View.VISIBLE
            viewModel.updateMeeting(tv_description_text.text.toString())
        }
    }

    private fun updateDateView() {
        tv_date_picker.text = viewModel.meetingDateCalendar.getDateInDateFormat()
        enableScheduleMeetingButton()
    }

    private fun updateStartTimeView() {
        if (viewModel.meetingStartTimeInMinutes >= 0) {
            tv_start_time_picker.text = getTimeFromTimeInMinutes(viewModel.meetingStartTimeInMinutes)
        }
        enableScheduleMeetingButton()
    }

    private fun updateEndTimeView() {
        if (viewModel.meetingEndTimeInMinutes >= 0) {
            tv_end_time_picker.text = getTimeFromTimeInMinutes(viewModel.meetingEndTimeInMinutes)
        }
        enableScheduleMeetingButton()
    }

    /**
     * enable schedule meeting button only when endtime is greater than start time
     *  and description text is not empty
     */
    private fun enableScheduleMeetingButton() {
        val enableButton =
            viewModel.meetingEndTimeInMinutes > viewModel.meetingStartTimeInMinutes
                    && tv_description_text.text.toString().isNotBlank()
                    && !viewModel.meetingDateCalendar.hasDateAlreadyPassed()
        bt_schedule_meeting.disableButton(!enableButton)
    }
}