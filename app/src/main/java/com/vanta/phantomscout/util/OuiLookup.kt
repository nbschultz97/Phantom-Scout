package com.vanta.phantomscout.util

import android.content.Context

/**
 * Offline-first IEEE OUI lookup.
 * Loads `oui.csv` from assets on demand and caches the prefix→vendor map.
 */
object OuiLookup {
    private val map = mutableMapOf<String, String>()
    @Volatile private var loaded = false

    private fun load(ctx: Context) {
        if (loaded) return
        runCatching {
            ctx.assets.open("oui.csv").bufferedReader().useLines { lines ->
                lines.filter { it.contains(',') }.forEach { line ->
                    val (prefix, vendor) = line.split(',', limit = 2)
                    map[prefix.uppercase().take(8)] = vendor.trim()
                }
            }
            loaded = true
        }
    }

    /** Return vendor for a MAC or null if unknown. */
    fun vendorFor(ctx: Context, mac: String?): String? {
        if (!loaded) load(ctx)
        return mac?.uppercase()?.take(8)?.let { map[it] }
    }
}
