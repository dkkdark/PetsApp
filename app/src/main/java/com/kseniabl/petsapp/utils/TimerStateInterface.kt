package com.kseniabl.petsapp.utils

interface TimerStateInterface {
    fun readEndTime(): Long
    fun readLeftTime(): Long
    fun saveTime(remainingTimeInMillis: Long)
}