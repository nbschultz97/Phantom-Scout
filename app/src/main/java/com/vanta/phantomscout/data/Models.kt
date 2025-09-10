package com.vanta.phantomscout.data

import com.vanta.phantomscout.sensing.self.RadioState

data class WifiBeacon(
    val bssid: String,
    val ssid: String,
    val channel: Int,
    val width: Int,
    val rssi: Int,
    val capabilities: String,
    val vendor: String?
)

data class BleAdvert(
    val address: String,
    val rssi: Int,
    val manufacturer: String?,
    val connectable: Boolean
)

data class Snapshot(
    val wifi: List<WifiBeacon>,
    val ble: List<BleAdvert>,
    val radio: RadioState
) {
    val wifiCount get() = wifi.size
    val wifiMaxRssi get() = wifi.maxOfOrNull { it.rssi } ?: -120
    val bleCount get() = ble.size
    val bleMaxRssi get() = ble.maxOfOrNull { it.rssi } ?: -120
    val hiddenSsids get() = wifi.any { it.ssid.isEmpty() }
}
