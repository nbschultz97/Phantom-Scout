package com.vanta.phantomscout.sensing.ble

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import com.vanta.phantomscout.data.BleAdvert
import com.vanta.phantomscout.util.OuiLookup

/** Low power BLE scanner exposing results as StateFlow. */
class BleScanner(ctx: Context, scope: CoroutineScope) {
    private val scanner: BluetoothLeScanner? =
        (ctx.getSystemService(Context.BLUETOOTH_SERVICE) as? android.bluetooth.BluetoothManager)?.adapter?.bluetoothLeScanner

    private val settings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
        .build()

    private val _adverts: StateFlow<List<BleAdvert>> = callbackFlow {
        val found = mutableMapOf<String, BleAdvert>()
        val cb = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val addr = result.device.address
                val advert = BleAdvert(
                    address = addr,
                    rssi = result.rssi,
                    manufacturer = OuiLookup.vendorFor(addr),
                    connectable = result.isConnectable
                )
                found[addr] = advert
                trySend(found.values.toList())
            }
        }
        try {
            scanner?.startScan(null, settings, cb)
        } catch (e: SecurityException) {
            close(e)
            return@callbackFlow
        }
        awaitClose { scanner?.stopScan(cb) }
    }.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList())

    /** Current set of discovered adverts. */
    val adverts: StateFlow<List<BleAdvert>> = _adverts
}
