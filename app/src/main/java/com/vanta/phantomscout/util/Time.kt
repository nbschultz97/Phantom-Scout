package com.vanta.phantomscout.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Time {
    private val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    fun now(): String = fmt.format(Date())
}
