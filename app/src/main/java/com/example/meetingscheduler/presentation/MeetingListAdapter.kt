package com.example.meetingscheduler.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meetingscheduler.data.MeetingDataModel
import com.example.meetingscheduler.R
import com.example.meetingscheduler.utils.ConfigurationUtils
import com.example.meetingscheduler.presentation.MeetingListAdapter.MeetingListViewHolder
import kotlinx.android.synthetic.main.meeting_item.view.*

/**
 * Adapter to render the list of meetings
 */
class MeetingListAdapter(val context: Context) : RecyclerView.Adapter<MeetingListViewHolder>() {

    private val meetingDataModel = mutableListOf<MeetingDataModel>()

    /**
     * update meeting data model
     */
    fun setDataModel(dataModel: List<MeetingDataModel>) {
        meetingDataModel.clear()
        meetingDataModel.addAll(dataModel)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.meeting_item, parent, false)
        return MeetingListViewHolder(view)
    }

    override fun getItemCount() = meetingDataModel.size

    override fun onBindViewHolder(holder: MeetingListViewHolder, position: Int) {
        holder.bind(meetingDataModel[position])
    }

    inner class MeetingListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText = view.timeText
        val startTimeText = view.startTimeText
        val endTimeText = view.endTimeText
        val description = view.descriptionText
        val participants = view.participants

        fun bind(dataModel: MeetingDataModel) {
            if (ConfigurationUtils.isLandScape(context)) {
                startTimeText.text = dataModel.startTime.formatTime()
                endTimeText.text = dataModel.endTime.formatTime()
                participants.text = ""
            } else {
                val time =
                    """${dataModel.startTime.formatTime()}-${dataModel.endTime.formatTime()}"""
                timeText.text = time
            }
            description.text = dataModel.description
        }
    }
}
