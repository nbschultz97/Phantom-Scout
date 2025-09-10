package com.vanta.phantomscout.data

import android.content.Context
import org.json.JSONArray

/** Simple profile loaded from guardian policies. */
data class Profile(
    val name: String,
    val policy: String
)

object Profiles {
    fun load(ctx: Context): List<Profile> {
        val arr = ctx.assets.open("guardian_policies.json").use { input ->
            JSONArray(input.bufferedReader().use { it.readText() })
        }
        val items = mutableListOf<Profile>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            items += Profile(o.getString("name"), o.getString("policy"))
        }
        return items
    }
}
