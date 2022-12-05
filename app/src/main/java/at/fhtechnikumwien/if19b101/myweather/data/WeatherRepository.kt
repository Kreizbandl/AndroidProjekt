package at.fhtechnikumwien.if19b101.myweather.data

import android.content.Context
import at.fhtechnikumwien.if19b101.myweather.data.WeatherItem
import at.fhtechnikumwien.if19b101.myweather.data.database.ApplicationDatabase

class WeatherRepository(context: Context) {

    //to backend
    private val weatherItemDao by lazy { ApplicationDatabase.getDatabase(context).weatherItemDao() }
    //to frontend
    val weatherItems by lazy { weatherItemDao.weatherItems }

    suspend fun updateOrInsertAll(weatherItems : List<WeatherItem>){
        weatherItemDao.updateOrInsertAll(weatherItems = weatherItems)
    }

    suspend fun deleteAll(){
        weatherItemDao.deleteAll()
    }
}