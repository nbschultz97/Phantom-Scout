package com.vanta.phantomscout.sensing

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.vanta.phantomscout.sensing.ble.BleScanner
import com.vanta.phantomscout.sensing.wifi.WifiScanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/** Foreground service that runs Wi-Fi and BLE scanners. */
class ScanningService : Service() {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wifi: WifiScanner
    private lateinit var ble: BleScanner

    override fun onCreate() {
        super.onCreate()
        wifi = WifiScanner(this)
        ble = BleScanner(this)
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIF_ID, buildNotification())
        scope.launch { wifi.scanFlow().collect() }
        scope.launch { ble.scanFlow().collect() }
        return START_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        val mgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, "Scanning", NotificationManager.IMPORTANCE_LOW)
        mgr.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Scanning active")
            .setContentText("Monitoring environment")
            .setSmallIcon(android.R.drawable.ic_menu_search)
            .build()

    companion object {
        private const val CHANNEL_ID = "scan"
        private const val NOTIF_ID = 1
    }
}

