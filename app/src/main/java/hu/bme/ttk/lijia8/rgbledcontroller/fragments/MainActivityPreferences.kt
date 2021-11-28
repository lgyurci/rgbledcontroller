package hu.bme.ttk.lijia8.rgbledcontroller.fragments

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import hu.bme.ttk.lijia8.rgbledcontroller.R

class MainActivityPreferences : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_activity_preference, rootKey)

        val ip = findPreference<EditTextPreference>("ip")
    //    ip?.summary = preferenceManager.findPreference<EditTextPreference>("ip")?.text.toString()
        ip?.summary = ip?.text.toString().trim()
        ip?.setOnPreferenceChangeListener { preference, newValue ->
            ip?.summary = newValue.toString().trim()
            true
        }
    }

}
