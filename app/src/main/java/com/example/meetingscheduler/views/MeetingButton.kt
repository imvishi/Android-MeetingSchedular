package com.example.meetingscheduler.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.TextViewCompat
import com.example.meetingscheduler.R

/**
 * Custom button used in this app
 */
class MeetingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.drawable.schedular_button)
    }

    /**
     * Method used to enable/disable the button
     */
    fun disableButton(shouldDisable: Boolean) {
        if (shouldDisable) {
            alpha = 0.2f
            isEnabled = false
        } else {
            alpha = 1.0f
            isEnabled = true
        }
    }
}