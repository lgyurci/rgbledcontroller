package hu.bme.ttk.lijia8.rgbledcontroller.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import hu.bme.ttk.lijia8.rgbledcontroller.R

class MainActivityPreferences : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_activity_preference, rootKey)
    }
}
