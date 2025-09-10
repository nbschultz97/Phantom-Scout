package com.vanta.phantomscout.util

/** Minimal OUI lookup table. Extend offline as needed. */
object OuiLookup {
    private val map = mapOf(
        "00:11:22" to "AcmeCorp",
        "AA:BB:CC" to "Contoso"
    )

    fun vendorFor(mac: String): String? = map[mac.uppercase().take(8)]
}
