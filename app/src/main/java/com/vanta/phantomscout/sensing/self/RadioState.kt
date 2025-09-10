package com.vanta.phantomscout.sensing.self

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.nfc.NfcAdapter
import android.provider.Settings

/** Snapshot of local radio states. */
data class RadioState(
    val wifiEnabled: Boolean,
    val bleEnabled: Boolean,
    val nfcEnabled: Boolean,
    val airplaneMode: Boolean
) {
    companion object {
        fun read(ctx: Context): RadioState {
            val wifi = android.net.wifi.WifiManager.WIFI_STATE_ENABLED ==
                (ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager).wifiState
            val ble = BluetoothAdapter.getDefaultAdapter()?.isEnabled == true
            val nfc = NfcAdapter.getDefaultAdapter(ctx)?.isEnabled == true
            val airplane = Settings.Global.getInt(ctx.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 1
            return RadioState(wifi, ble, nfc, airplane)
        }
    }
}
