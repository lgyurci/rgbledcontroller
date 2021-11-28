package hu.bme.ttk.lijia8.rgbledcontroller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.ttk.lijia8.rgbledcontroller.RGBApplication
import hu.bme.ttk.lijia8.rgbledcontroller.database.RoomRGB
import hu.bme.ttk.lijia8.rgbledcontroller.repository.Repository
import kotlinx.coroutines.launch

class RGBViewModel : ViewModel() {

    private val repository: Repository

    val allColors: LiveData<List<RoomRGB>>

    init {
        val todoDao = RGBApplication.rgbDatabase.rgbDao()
        repository = Repository(todoDao)
        allColors = repository.getAllColors()
    }

    fun insert(color: RoomRGB) = viewModelScope.launch {
        repository.insert(color)
    }

    fun delete(color: RoomRGB) = viewModelScope.launch {
        repository.delete(color)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}