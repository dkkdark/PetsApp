package com.kseniabl.petsapp.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerState @Inject constructor(
    @ApplicationContext var context: Context
): TimerStateInterface {

    override fun readEndTime(): Long {
        val sharedPref = context.getSharedPreferences("shared_pref_mils", Context.MODE_PRIVATE)
        return sharedPref.getLong("endTime", 0)
    }

    override fun readLeftTime(): Long {
        val sharedPref = context.getSharedPreferences("shared_pref_mils", Context.MODE_PRIVATE)
        return sharedPref.getLong("mils", START_TIME_IN_MILLIS)
    }

     override fun saveTime(remainingTimeInMillis: Long) {
        val sharedPref = context.getSharedPreferences("shared_pref_mils", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putLong("mils", remainingTimeInMillis)
            putLong("endTime", System.currentTimeMillis())
            apply()
        }
    }

    companion object {
        const val START_TIME_IN_MILLIS = 36000L
    }
}