package com.vanta.phantomscout.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vanta.phantomscout.data.Profiles

/** Lists guardian profiles. */
class ProfilesFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profiles = Profiles.load(requireContext())
        // TODO: show profiles in UI
    }
}
