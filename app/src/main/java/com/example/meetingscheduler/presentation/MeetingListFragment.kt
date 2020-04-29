package com.example.meetingscheduler.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meetingscheduler.R
import com.example.meetingscheduler.utils.getDateInDateFormat
import com.example.meetingscheduler.utils.hasDateAlreadyPassed
import kotlinx.android.synthetic.main.main_fragment.*

/**
 * Fragment to show the list of meetings for any particular day.
 */
class MeetingListFragment : Fragment() {

    companion object {
        fun newInstance() = MeetingListFragment()
        private const val TAG = "MeetingListFragment"
    }

    private lateinit var viewModel: SchedulerViewModel
    private lateinit var meetingListAdapter: MeetingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SchedulerViewModel::class.java).also {
            it.getCurrentDaySchedule()
            it.meetingScheduleLiveData.observe(this, Observer {
                loadingProgressBar.visibility = View.GONE
                meetingList.visibility = View.VISIBLE
                meetingListAdapter.setDataModel(it)
            })
        }
        meetingListAdapter = MeetingListAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        meetingList.apply {
            adapter = meetingListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        updateView()
        nextDate.setOnClickListener {
            viewModel.getNextDaySchedule()
            updateView()
        }
        prevDate.setOnClickListener {
            viewModel.getPrevDaySchedule()
            updateView()
        }
        scheduleButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ScheduleMeetingFragment.newInstance())
                .addToBackStack(TAG)
                .commit()
        }
    }

    private fun updateView() {
        dateText.text = viewModel.calendar.getDateInDateFormat()
        loadingProgressBar.visibility = View.VISIBLE
        meetingList.visibility = View.GONE
        enableScheduleButton()
    }

    private fun enableScheduleButton() {
        if (viewModel.calendar.hasDateAlreadyPassed()) {
            scheduleButton.apply {
                alpha = 0.2f
                isEnabled = false
            }
        } else {
            scheduleButton.apply {
                alpha = 1.0f
                isEnabled = true
            }
        }
    }
}
