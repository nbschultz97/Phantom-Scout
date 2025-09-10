package com.vanta.phantomscout.advisory

import com.vanta.phantomscout.advisory.RiskModel.Bucket
import com.vanta.phantomscout.data.Snapshot

/**
 * Simple additive risk model that scores RF exposure based on current snapshot.
 * Returns a 0-100 score, bucketed severity, and the top contributing reasons.
 */
object RiskModel {
    enum class Bucket { GREEN, AMBER, RED }

    data class Result(val score: Int, val bucket: Bucket, val reasons: List<String>)

    fun assess(s: Snapshot): Result {
        val factors = mutableListOf<Pair<Int, String>>()

        if (s.radio.wifiEnabled) factors += 15 to "Wi-Fi radio active"
        if (s.radio.bleEnabled) factors += 10 to "Bluetooth radio active"
        if (s.wifiMaxRssi >= -45) factors += 15 to "Strong Wi-Fi signal (${s.wifiMaxRssi} dBm)"
        if (s.bleMaxRssi >= -55) factors += 10 to "Strong BLE signal (${s.bleMaxRssi} dBm)"
        if (s.wifiCount >= 10) factors += 10 to "High Wi-Fi device count (${s.wifiCount})"
        if (s.bleCount >= 15) factors += 10 to "High BLE device count (${s.bleCount})"
        if (s.hiddenSsids) factors += 20 to "Hidden SSIDs present"

        val density = if (s.wifiCount == 0) s.bleCount.toDouble() else s.bleCount.toDouble() / s.wifiCount
        if (density >= 2.0) {
            val densityStr = "%.1f".format(density)
            factors += 10 to "High BLE density (${densityStr}x Wi-Fi)"
        }

        val total = factors.sumOf { it.first }.coerceAtMost(100)
        val bucket = when {
            total <= 25 -> Bucket.GREEN
            total <= 60 -> Bucket.AMBER
            else -> Bucket.RED
        }
        val topReasons = factors.sortedByDescending { it.first }.take(3).map { it.second }
        return Result(total, bucket, topReasons)
    }
}

