package com.example.meetingscheduler.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meetingscheduler.Data.MeetingDataModel
import com.example.meetingscheduler.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.meeting_schedule_fragment.*

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
        viewModel = ViewModelProvider(requireActivity()).get(SchedulerViewModel::class.java)
        meetingListAdapter = MeetingListAdapter(requireContext())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        meetingListAdapter.setDataModel(mockListData())
        meetingList.apply {
            adapter = meetingListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        scheduleButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ScheduleMeetingFragment.newInstance())
                .addToBackStack(TAG)
                .commit()
        }
    }

    private fun mockListData() = mutableListOf<MeetingDataModel>().apply {
        add(MeetingDataModel("11:00AM","12:00PM", "afsafsafsafsafasfsa","sdfsdfsaf,fsfsdf,sfsdf"))
        add(MeetingDataModel("14:40PM","15:00PM", "afsafsafsafsafasfsa","sf ds,dsfdfsf"))
        add(MeetingDataModel("18:00PM","18:40PM", "afsafsafsafsafasfsa","sdfdf,dfd"))
        add(MeetingDataModel("23:30PM","23:30PM", "afsafsafsafsafasfsa","sdfdsf,d,d,d,dd"))
        add(MeetingDataModel("18:00PM","18:40PM", "afsafsafsafsafasfsa","sdfdf,dfd"))
        add(MeetingDataModel("23:30PM","23:30PM", "afsafsafsafsafasfsa","sdfdsf,d,d,d,dd"))
    }
}
