package com.vanta.phantomscout.advisory

import com.vanta.phantomscout.data.Profile
import com.vanta.phantomscout.data.Snapshot

/** Applies policies to snapshots to suggest fixes. */
object RulesEngine {
    fun evaluate(s: Snapshot, p: Profile): List<FixAction> {
        val fixes = mutableListOf<FixAction>()
        if (p.policy == "all_radio_off") {
            if (s.radio.wifiEnabled) fixes += FixAction("Turn off Wi-Fi")
            if (s.radio.bleEnabled) fixes += FixAction("Turn off Bluetooth")
        }
        return fixes
    }
}
