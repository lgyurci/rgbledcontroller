package hu.bme.ttk.lijia8.rgbledcontroller.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import hu.bme.ttk.lijia8.rgbledcontroller.R
import hu.bme.ttk.lijia8.rgbledcontroller.network.ArduinoNetworkAPI
import hu.bme.ttk.lijia8.rgbledcontroller.singletons.CurrentRGB

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

        findPreference<Preference>("concheck")?.setOnPreferenceClickListener {
            val api = ArduinoNetworkAPI(requireContext())
            asyncTest { api.checkConnection() }
            true
        }
    }

    private fun asyncTest(call: () -> Boolean) {
        Thread {
            try {
                val b = call()
                if (b){
                    requireActivity().runOnUiThread{
                        val toast = Toast.makeText(context,getString(R.string.working_connection),Toast.LENGTH_SHORT)
                        toast.show()
                    }
                } else {
                    requireActivity().runOnUiThread{
                        val toast = Toast.makeText(context,getString(R.string.connection_fail),Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            } catch (e: Exception){
            //    requireActivity().runOnUiThread{
            //        val toast = Toast.makeText(context,getString(R.string.connection_fail),Toast.LENGTH_SHORT)
            //        toast.show()
            //    }
            }
        }.start()
    }

}
