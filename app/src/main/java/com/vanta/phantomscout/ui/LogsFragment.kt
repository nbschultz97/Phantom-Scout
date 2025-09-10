package com.vanta.phantomscout.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vanta.phantomscout.data.LogStore
import com.vanta.phantomscout.data.Snapshot

/** Controls logging of snapshots. */
class LogsFragment : Fragment() {
    private var logging = false
    private lateinit var store: LogStore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        store = LogStore(requireContext())
    }

    fun toggleLogging(s: Snapshot, risk: Int) {
        if (!logging) logging = true
        store.appendCsv(s, risk)
    }

    fun export() = store.exportJson()
}
