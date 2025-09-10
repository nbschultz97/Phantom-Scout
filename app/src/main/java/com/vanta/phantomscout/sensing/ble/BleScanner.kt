package com.vanta.phantomscout.sensing.ble

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.vanta.phantomscout.data.BleAdvert
import com.vanta.phantomscout.util.OuiLookup

/** Low power BLE scanner using callbackFlow. */
class BleScanner(ctx: Context) {
    private val scanner: BluetoothLeScanner? =
        (ctx.getSystemService(Context.BLUETOOTH_SERVICE) as? android.bluetooth.BluetoothManager)?.adapter?.bluetoothLeScanner

    fun scanFlow(): Flow<List<BleAdvert>> = callbackFlow {
        val found = mutableMapOf<String, BleAdvert>()
        val cb = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val addr = result.device.address
                val advert = BleAdvert(
                    address = addr,
                    rssi = result.rssi,
                    manufacturer = result.scanRecord?.manufacturerSpecificData?.keyAt(0)
                        ?.let { OuiLookup.vendorFor(addr) },
                    connectable = result.isConnectable
                )
                found[addr] = advert
                trySend(found.values.toList())
            }
        }
        scanner?.startScan(null, android.bluetooth.le.ScanSettings.Builder().setScanMode(android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_POWER).build(), cb)
        awaitClose { scanner?.stopScan(cb) }
    }
}
