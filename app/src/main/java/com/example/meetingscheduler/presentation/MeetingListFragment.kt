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

    private lateinit var viewModel: MeetingViewModel
    private lateinit var meetingListAdapter: MeetingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        meetingListAdapter = MeetingListAdapter()
        viewModel = ViewModelProvider(requireActivity()).get(MeetingViewModel::class.java).also {
                it.meetingScheduleLiveData.observe(this, Observer {
                    pb_loading.visibility = View.GONE
                    rv_meeting_list.visibility = View.VISIBLE
                    meetingListAdapter.setDataModel(it)
                })
            }

        if (savedInstanceState == null) {
            viewModel.getCurrentDaySchedule()
        }

        parentFragmentManager.addOnBackStackChangedListener {
            if (parentFragmentManager.backStackEntryCount == 0) {
                //Refresh the meeting list when meetingListFragment popped from back stack
                viewModel.getCurrentDaySchedule()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_meeting_list.apply {
            adapter = meetingListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        updateView()
        ll_next_date.setOnClickListener {
            viewModel.getNextDaySchedule()
            updateView()
        }
        ll_prev_date.setOnClickListener {
            viewModel.getPrevDaySchedule()
            updateView()
        }
        bt_schedule.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ScheduleMeetingFragment.newInstance(), TAG)
                .addToBackStack(TAG)
                .commit()
        }
    }

    private fun updateView() {
        tv_date_text.text = viewModel.meetingDateCalendar.getDateInDateFormat()
        pb_loading.visibility = View.VISIBLE
        rv_meeting_list.visibility = View.GONE
        bt_schedule.disableButton(shouldDisable = viewModel.meetingDateCalendar.hasDateAlreadyPassed())
    }
}
