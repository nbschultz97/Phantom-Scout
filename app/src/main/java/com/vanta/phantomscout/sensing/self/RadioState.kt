package com.vanta.phantomscout.sensing.self

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.os.Build
import android.provider.Settings
import android.uwb.UwbManager

/** Snapshot of local radio states. */
data class RadioState(
    val wifiEnabled: Boolean,
    val bleEnabled: Boolean,
    val nfcEnabled: Boolean,
    val uwbEnabled: Boolean,
    val airplaneMode: Boolean
) {
    companion object {
        fun read(ctx: Context): RadioState {
            val wifiManager = ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
            val wifi = wifiManager?.isWifiEnabled == true

            val bleAdapter = (ctx.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?)?.adapter
            val ble = bleAdapter?.isEnabled == true

            val nfc = NfcAdapter.getDefaultAdapter(ctx)?.isEnabled == true

            val uwb = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val uwbManager = ctx.getSystemService(UwbManager::class.java)
                uwbManager?.isUwbEnabled == true
            } else {
                false
            }

            val airplane = Settings.Global.getInt(
                ctx.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) == 1

            return RadioState(wifi, ble, nfc, uwb, airplane)
        }

        fun wifiSettingsIntent() = Intent(Settings.ACTION_WIFI_SETTINGS)
        fun bleSettingsIntent() = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        fun nfcSettingsIntent() = Intent(Settings.ACTION_NFC_SETTINGS)
        fun uwbSettingsIntent() = Intent(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                Settings.ACTION_UWB_SETTINGS
            } else {
                Settings.ACTION_WIRELESS_SETTINGS
            }
        )
        fun airplaneSettingsIntent() = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
    }
}
