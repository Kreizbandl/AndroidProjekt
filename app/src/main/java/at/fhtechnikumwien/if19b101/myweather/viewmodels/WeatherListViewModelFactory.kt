package at.fhtechnikumwien.if19b101.myweather.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.fhtechnikumwien.if19b101.myweather.data.WeatherRepository
import java.lang.IllegalArgumentException

class WeatherListViewModelFactory(private val application: Application, private val weatherRepository : WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return WeatherListViewModel(application = application, weatherRepository = weatherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}