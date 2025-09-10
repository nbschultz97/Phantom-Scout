package com.vanta.phantomscout.advisory

import com.vanta.phantomscout.data.Snapshot

/** Simple textual mitigations based on snapshot. */
object Mitigations {
    fun advice(s: Snapshot): List<String> {
        val msgs = mutableListOf<String>()
        if (s.radio.wifiEnabled) msgs += "Disable Wi-Fi"
        if (s.radio.bleEnabled) msgs += "Disable Bluetooth"
        if (s.wifiCount >= 10 || s.bleCount >= 15) msgs += "Minimize dwell; move to RF shadow"
        if (s.hiddenSsids) msgs += "Avoid active scans; keep Wi-Fi off"
        return msgs
    }
}
