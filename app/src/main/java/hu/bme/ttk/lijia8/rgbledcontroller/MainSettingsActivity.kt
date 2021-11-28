package hu.bme.ttk.lijia8.rgbledcontroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.ActivityMainBinding
import hu.bme.ttk.lijia8.rgbledcontroller.databinding.ActivityMainSettingsBinding
import hu.bme.ttk.lijia8.rgbledcontroller.fragments.MainActivityPreferences

class MainSettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_frame_layout, MainActivityPreferences())
            .commit()
    }
}