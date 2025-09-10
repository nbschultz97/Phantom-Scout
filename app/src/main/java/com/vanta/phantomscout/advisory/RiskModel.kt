package com.vanta.phantomscout.advisory

import com.vanta.phantomscout.data.Snapshot
import com.vanta.phantomscout.advisory.RiskModel.Bucket

/** Simple additive risk model. */
object RiskModel {
    enum class Bucket { GREEN, AMBER, RED }

    fun score(s: Snapshot): Pair<Int, Bucket> {
        var total = 0
        if (s.radio.wifiEnabled) total += 10
        if (s.radio.bleEnabled) total += 10
        if (s.wifiMaxRssi >= -45) total += 15
        if (s.bleMaxRssi >= -55) total += 10
        if (s.wifiCount >= 10) total += 10
        if (s.bleCount >= 15) total += 10
        if (total > 100) total = 100
        val bucket = when {
            total <= 25 -> Bucket.GREEN
            total <= 60 -> Bucket.AMBER
            else -> Bucket.RED
        }
        return total to bucket
    }
}
