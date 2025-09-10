package com.vanta.phantomscout.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vanta.phantomscout.sensing.self.RadioState

/** Shows current radio states with quick links to settings. */
class MyPhoneFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val state = RadioState.read(requireContext())
        // TODO: update UI with state and settings intents
    }
}
