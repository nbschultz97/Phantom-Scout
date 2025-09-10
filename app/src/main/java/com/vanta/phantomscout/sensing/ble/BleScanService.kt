package com.vanta.phantomscout.sensing.ble

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/** Service hosting a low power BLE scan. */
class BleScanService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var scanner: BleScanner

    private val binder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        scanner = BleScanner(this, scope)
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    inner class LocalBinder : Binder() {
        fun scanner(): BleScanner = scanner
    }
}
