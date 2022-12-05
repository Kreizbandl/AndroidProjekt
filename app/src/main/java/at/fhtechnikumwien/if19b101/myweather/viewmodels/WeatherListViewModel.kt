package at.fhtechnikumwien.if19b101.myweather.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import at.fhtechnikumwien.if19b101.myweather.R
import at.fhtechnikumwien.if19b101.myweather.data.WeatherRepository
import at.fhtechnikumwien.if19b101.myweather.data.download.WeatherDownloader
import kotlinx.coroutines.launch

class WeatherListViewModel(application : Application, private val weatherRepository : WeatherRepository) : AndroidViewModel(application) {

    val weatherItems by lazy { weatherRepository.weatherItems }

    //download data from web as list and forward it to repository -> db
    private fun downloadWeatherItems(urlString: String, delete: Boolean){
        viewModelScope.launch {
            if (delete){
                weatherRepository.deleteAll()
            }
            val weatherItems = WeatherDownloader().load(urlString)
            if (weatherItems != null){
                weatherRepository.updateOrInsertAll(weatherItems)
            }
        }
    }

    //helper function get location from settings
    private fun getLocation(): String {
        val context = getApplication<Application>().applicationContext
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(
            context.getString(R.string.settings_location_key),
            context.resources.getString(R.string.settings_location_default_value))
            ?: context.resources.getString(R.string.settings_location_default_value)
    }

    //create http url to openweather api
    fun reload(delete: Boolean){
        downloadWeatherItems("https://api.openweathermap.org/data/2.5/forecast?q=" + getLocation() + "&appid=c9ec7faaf1478ff72ce5242bc5f37fc6&units=metric", delete)
    }
}