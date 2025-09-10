package com.vanta.phantomscout.data

import android.content.Context
import java.io.File
import java.io.FileWriter
import com.google.gson.Gson

/** Simple CSV/JSON log writer for snapshots. */
class LogStore(private val ctx: Context) {
    private val gson = Gson()
    private val dir: File = File(ctx.filesDir, "logs").apply { mkdirs() }
    private val csv = File(dir, "scan.csv")

    fun appendCsv(s: Snapshot, risk: Int) {
        val line = listOf(
            System.currentTimeMillis(), risk,
            s.wifiCount, s.wifiMaxRssi,
            s.bleCount, s.bleMaxRssi,
            s.radio.wifiEnabled, s.radio.bleEnabled, s.radio.nfcEnabled
        ).joinToString(",")
        FileWriter(csv, true).use { it.appendLine(line) }
    }

    fun exportJson(): File {
        val json = gson.toJson(csv.readLines())
        val out = File(dir, "scan.json")
        out.writeText(json)
        return out
    }
}
