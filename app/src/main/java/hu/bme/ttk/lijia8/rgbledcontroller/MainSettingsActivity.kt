package hu.bme.ttk.lijia8.rgbledcontroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.ttk.lijia8.rgbledcontroller.fragments.MainActivityPreferences

class MainSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_settings)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_frame_layout, MainActivityPreferences())
            .commit()
    }
}