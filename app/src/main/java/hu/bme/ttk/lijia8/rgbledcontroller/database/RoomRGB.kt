package hu.bme.ttk.lijia8.rgbledcontroller.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rgbpresets")
data class RoomRGB(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val red :Int = 0,
    val green :Int = 0,
    val blue :Int = 0
)