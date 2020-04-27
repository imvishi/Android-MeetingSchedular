package com.example.meetingscheduler.Utils

import android.content.Context
import android.content.res.Configuration

/**
 * Class used to provide the utility methods related to device configuration
 */
object ConfigurationUtils {

    fun isLandScape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}