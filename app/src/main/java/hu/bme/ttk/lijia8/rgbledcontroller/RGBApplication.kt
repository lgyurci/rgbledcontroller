package hu.bme.ttk.lijia8.rgbledcontroller

import android.app.Application
import androidx.room.Room
import hu.bme.ttk.lijia8.rgbledcontroller.database.RGBDatabase

class RGBApplication : Application() {

    companion object {
        lateinit var rgbDatabase: RGBDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        rgbDatabase = Room.databaseBuilder(
            applicationContext,
            RGBDatabase::class.java,
            "rgb_database"
        ).fallbackToDestructiveMigration().build()
    }

}