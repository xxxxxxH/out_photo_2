package net.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

class ScreenManager {
    companion object {
        private var instance: ScreenManager? = null
            get() {
                field ?: run {
                    field = ScreenManager()
                }
                return field
            }

        @Synchronized
        fun get(): ScreenManager {
            return instance!!
        }
    }

    fun getScreenSize(activity: Activity):IntArray{
        val result = IntArray(2)
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        result[0] = height
        result[1] = width
        return result
    }
}