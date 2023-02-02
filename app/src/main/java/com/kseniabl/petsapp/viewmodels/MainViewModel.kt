package com.kseniabl.petsapp.viewmodels

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.kseniabl.petsapp.ui.TransactionItem
import com.kseniabl.petsapp.ui.transactionsList
import com.kseniabl.petsapp.utils.TimerStateInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class MainViewModel @Inject constructor(
    private val timerState: TimerStateInterface
): ViewModel() {

    private val _time = MutableStateFlow("00:00")
    val time: StateFlow<String> = _time

    val transactions = transactionsList

    private var remainingTimeInMillis = 0L
    private var cTimer: CountDownTimer? = null

    private fun startTimer() {
        cTimer = object : CountDownTimer(remainingTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                updateCountDownText()
            }
            override fun onFinish() {
                resetTimer()
            }
        }
        cTimer?.start()
    }

    fun cancelTimer() {
        cTimer?.cancel()
    }

    private fun resetTimer() {
        remainingTimeInMillis = 0
        updateCountDownText()
    }

    private fun updateCountDownText() {
        val minutes = (remainingTimeInMillis / 1000) / 60
        val seconds = (remainingTimeInMillis / 1000) % 60
        val timeLeftFormatted =
            java.lang.String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        _time.value = timeLeftFormatted
    }

    fun saveTime() {
        timerState.saveTime(remainingTimeInMillis)
    }

    fun readTime() {
        val endTime = timerState.readEndTime()
        val mTimeLeftInMillis = timerState.readLeftTime()
        if (mTimeLeftInMillis == 0L)
            return

        if (endTime == 0L)
            remainingTimeInMillis = mTimeLeftInMillis
        else {
            val timeDiff = abs(endTime - System.currentTimeMillis())

            remainingTimeInMillis = mTimeLeftInMillis - timeDiff
            var timeDiffInMillisPlusTimerRemaining = remainingTimeInMillis
            if (timeDiffInMillisPlusTimerRemaining > 0) {
                timeDiffInMillisPlusTimerRemaining = abs(timeDiffInMillisPlusTimerRemaining)
                remainingTimeInMillis = timeDiffInMillisPlusTimerRemaining
            }
            else
                remainingTimeInMillis = 0
        }
        updateCountDownText()
        startTimer()
    }
}