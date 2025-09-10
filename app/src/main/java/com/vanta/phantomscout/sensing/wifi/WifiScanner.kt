package com.vanta.phantomscout.sensing.wifi

import android.content.Context
import android.net.wifi.WifiManager
import com.vanta.phantomscout.data.WifiBeacon
import com.vanta.phantomscout.util.OuiLookup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Passive Wi-Fi scanner using Android's [WifiManager].
 * Polls every 12 seconds and emits results via [StateFlow].
 */
class WifiScanner(ctx: Context, scope: CoroutineScope) {
    private val wifi = ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val _beacons = MutableStateFlow<List<WifiBeacon>>(emptyList())
    val beacons: StateFlow<List<WifiBeacon>> = _beacons.asStateFlow()

    init {
        scope.launch(Dispatchers.IO) {
            while (isActive) {
                _beacons.value = scan()
                delay(12_000L)
            }
        }
    }

    /** Perform a single scan converting results to [WifiBeacon] objects. */
    suspend fun scan(): List<WifiBeacon> = withContext(Dispatchers.IO) {
        wifi.scanResults
            .distinctBy { it.BSSID }
            .map {
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

