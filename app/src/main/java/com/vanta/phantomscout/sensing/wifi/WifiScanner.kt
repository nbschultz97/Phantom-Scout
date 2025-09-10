package com.vanta.phantomscout.sensing.wifi

import android.content.Context
import android.net.wifi.WifiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import com.vanta.phantomscout.data.WifiBeacon
import com.vanta.phantomscout.util.OuiLookup

/**
 * Passive Wi-Fi scan using system WiFiManager.
 * Designed for low-duty cycle polling (~10s) to conserve power.
 */
class WifiScanner(private val ctx: Context) {
    private val wifi = ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    suspend fun scan(): List<WifiBeacon> = withContext(Dispatchers.IO) {
        val results = wifi.scanResults
        results.map {
            WifiBeacon(
                bssid = it.BSSID,
                ssid = it.SSID ?: "",
                channel = it.frequency.toChannel(),
                width = it.channelWidth.toHz(),
                rssi = it.level,
                capabilities = it.capabilities,
                vendor = OuiLookup.vendorFor(it.BSSID)
            )
        }
    }

    /** Simple flow emitting scan results every [intervalMs]. */
    fun scanFlow(intervalMs: Long = 10_000L): Flow<List<WifiBeacon>> = flow {
        while (true) {
            emit(scan())
            kotlinx.coroutines.delay(intervalMs)
        }
    }
}

private fun Int.toChannel(): Int = when (this) {
    in 2412..2484 -> (this - 2407) / 5
    in 5170..5865 -> (this - 5000) / 5
    else -> -1
}

private fun Int.toHz(): Int = when (this) {
    WifiManager.CHANNEL_WIDTH_20MHZ -> 20
    WifiManager.CHANNEL_WIDTH_40MHZ -> 40
    WifiManager.CHANNEL_WIDTH_80MHZ -> 80
    WifiManager.CHANNEL_WIDTH_160MHZ -> 160
    else -> 20
}
