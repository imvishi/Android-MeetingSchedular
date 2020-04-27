package com.example.meetingscheduler.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.meetingscheduler.R
import com.example.meetingscheduler.utils.DateTimePickerManager
import com.example.meetingscheduler.utils.DateTimePickerManager.showDatePicker
import com.example.meetingscheduler.utils.DateTimePickerManager.showTimePicker
import kotlinx.android.synthetic.main.meeting_schedule_fragment.*
import java.util.*

/**
 * Fragment used to schedule the meeting
 */
class ScheduleMeetingFragment : Fragment() {

    companion object {
        fun newInstance() = ScheduleMeetingFragment()
    }

    private val calendar = Calendar.getInstance()
    private fun onDateUpdated(calendar: Calendar) {
        Log.d("***", calendar.toString())
    }

    private fun onTimeUpdated(calendar: Calendar) {
        Log.d("***", calendar.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DateTimePickerManager.let {
            it.dateUpdatedLiveData.observe(this, Observer(::onDateUpdated))
            it.timeUpdatedLiveData.observe(this, Observer(::onTimeUpdated))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.meeting_schedule_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener { requireActivity().onBackPressed() }
        datePickerView.setOnClickListener { showDatePicker(calendar, requireContext()) }
        startTimePickerView.setOnClickListener { showTimePicker(calendar, requireContext()) }
        endTimePickerView.setOnClickListener { showTimePicker(calendar, requireContext()) }
    }
}