package com.mvd.drunkgames.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.mvd.drunkgames.R

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}