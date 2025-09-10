package com.vanta.phantomscout.data

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileWriter
import com.google.gson.Gson

/**
 * Persistent log writer. Stores compact CSV metrics alongside full
 * JSON snapshots capturing leaks and mitigation outcomes.
 */
class LogStore(private val ctx: Context) {
    private val gson = Gson()
    private val dir: File = File(ctx.filesDir, "logs").apply { mkdirs() }
    private val csv = File(dir, "scan.csv")
    private val jsonl = File(dir, "scan.jsonl")

    fun append(s: Snapshot, risk: Int, leaks: List<String>, fixes: Map<String, Boolean>) {
        val line = listOf(
            System.currentTimeMillis(), risk,
            s.wifiCount, s.wifiMaxRssi,
            s.bleCount, s.bleMaxRssi,
            s.radio.wifiEnabled, s.radio.bleEnabled, s.radio.nfcEnabled
        ).joinToString(",")
        FileWriter(csv, true).use { it.appendLine(line) }

        val entry = AarEntry(System.currentTimeMillis(), s, leaks, fixes)
        FileWriter(jsonl, true).use { it.appendLine(gson.toJson(entry)) }
    }

    /** Export After Action Report to /Documents/PhantomScout/Reports/. */
    fun exportAar(): File {
        val docs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val reports = File(docs, "PhantomScout/Reports").apply { mkdirs() }
        val out = File(reports, "aar-${System.currentTimeMillis()}.json")
        val payload = if (jsonl.exists())
            jsonl.readLines().joinToString(prefix = "[", postfix = "]", separator = ",")
        else "[]"
        out.writeText(payload)
        return out
    }
}

