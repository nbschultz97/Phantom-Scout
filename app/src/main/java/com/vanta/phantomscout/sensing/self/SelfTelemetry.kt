package com.vanta.phantomscout.sensing.self

import android.content.Context
import android.os.BatteryManager
import android.os.Build
import android.os.SystemClock

/** Basic device telemetry snapshot. */
data class SelfTelemetry(
    val uptimeMs: Long,
    val batteryPct: Int,
    val model: String = Build.MODEL
) {
    companion object {
        fun read(ctx: Context): SelfTelemetry {
            val bm = ctx.getSystemService(BatteryManager::class.java)
            val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: -1
            val uptime = SystemClock.elapsedRealtime()
            return SelfTelemetry(uptime, level)
        }
    }
}
