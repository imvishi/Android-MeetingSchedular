package com.example.meetingscheduler.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meetingscheduler.data.MeetingScheduleDataModel
import com.example.meetingscheduler.R
import com.example.meetingscheduler.utils.ConfigurationUtils
import com.example.meetingscheduler.presentation.MeetingListAdapter.MeetingListViewHolder
import com.example.meetingscheduler.utils.TimeUtils.getTimeFromTimeInMinutes
import kotlinx.android.synthetic.main.meeting_item.view.*

/**
 * Adapter to render the list of meetings
 */
class MeetingListAdapter : RecyclerView.Adapter<MeetingListViewHolder>() {

    private val meetingDataModel = mutableListOf<MeetingScheduleDataModel>()

    /**
     * update meeting data model
     */
    fun setDataModel(dataModel: List<MeetingScheduleDataModel>) {
        meetingDataModel.clear()
        meetingDataModel.addAll(dataModel)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meeting_item, parent, false)
        return MeetingListViewHolder(view)
    }

    override fun getItemCount() = meetingDataModel.size

    override fun onBindViewHolder(holder: MeetingListViewHolder, position: Int) {
        holder.bind(meetingDataModel[position])
    }

    inner class MeetingListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val timeText = view.tv_time_text
        val startTimeText = view.tv_start_time
        val endTimeText = view.tv_end_time
        val description = view.tv_description_text
        val participants = view.tv_participants

        fun bind(dataModel: MeetingScheduleDataModel) {
            if (ConfigurationUtils.isLandScape(view.context)) {
                startTimeText.text = getTimeFromTimeInMinutes(dataModel.startTimeInMinutes)
                endTimeText.text = getTimeFromTimeInMinutes(dataModel.endTimeInMinutes)
                participants.text = ""
            } else {
                val time =
                    """${getTimeFromTimeInMinutes(dataModel.startTimeInMinutes)}-${getTimeFromTimeInMinutes(
                        dataModel.endTimeInMinutes
                    )}"""
                timeText.text = time
            }
            description.text = dataModel.description
        }
    }
}
