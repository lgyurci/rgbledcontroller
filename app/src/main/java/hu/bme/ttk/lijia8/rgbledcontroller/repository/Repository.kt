package hu.bme.ttk.lijia8.rgbledcontroller.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import hu.bme.ttk.lijia8.rgbledcontroller.database.RGBDao
import hu.bme.ttk.lijia8.rgbledcontroller.database.RoomRGB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val rgbDao: RGBDao) {

    fun getAllColors(): LiveData<List<RoomRGB>> {
        return rgbDao.getAllColors()
            .map {roomRGB ->
                roomRGB.map {roomRGB ->
                    roomRGB.toDomainModel() }
            }
    }

    suspend fun insert(color: RoomRGB) = withContext(Dispatchers.IO) {
        rgbDao.insertColor(color.toRoomModel())
    }

    suspend fun delete(color: RoomRGB) = withContext(Dispatchers.IO) {
        val roomRGB = rgbDao.getColorById(color.id) ?: return@withContext
        rgbDao.deleteColor(roomRGB)
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO){
        rgbDao.deleteAll()
    }

    private fun RoomRGB.toDomainModel(): RoomRGB {
        return RoomRGB(
            id = id,
            red = red,
            green = green,
            blue = blue
        )
    }

    private fun RoomRGB.toRoomModel(): RoomRGB {
        return RoomRGB(
            red = red,
            green = green,
            blue = blue
        )
    }
}