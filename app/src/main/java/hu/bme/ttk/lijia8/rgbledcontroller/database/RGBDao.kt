package hu.bme.ttk.lijia8.rgbledcontroller.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RGBDao {
    @Insert
    fun insertColor(color: RoomRGB)

    @Query("SELECT * FROM rgbpresets")
    fun getAllColors(): LiveData<List<RoomRGB>>

    @Delete
    fun deleteColor(color: RoomRGB)

    @Query("SELECT * FROM rgbpresets WHERE id == :id")
    fun getColorById(id: Int?): RoomRGB?

    @Query("DELETE FROM rgbpresets")
    fun deleteAll()

}