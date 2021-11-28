package hu.bme.ttk.lijia8.rgbledcontroller.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = false,
    entities = [RoomRGB::class]
)
abstract class RGBDatabase : RoomDatabase() {

    abstract fun rgbDao(): RGBDao

}