package at.fhtechnikumwien.if19b101.myweather.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import at.fhtechnikumwien.if19b101.myweather.data.WeatherItem
import kotlin.collections.ArrayList

@Dao
abstract class WeatherItemDao {
    //define direct DB operations
    @get:Query("SELECT * FROM weather_item")
    abstract val weatherItems : LiveData<List<WeatherItem>>
    @Query("SELECT _id FROM weather_item WHERE date = :date")
    abstract suspend fun getIdForDate(date : String) : Long
    @Update
    abstract suspend fun update(newsItems: List<WeatherItem>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun tryInsert(weatherItems: List<WeatherItem>): List<Long>
    @Query("DELETE FROM weather_item")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun updateOrInsertAll(weatherItems: List<WeatherItem>){
        val insertResult: List<Long> = tryInsert(weatherItems)
        val updateList: MutableList<WeatherItem> = ArrayList()
        for (i in insertResult.indices) {
            if(insertResult[i] == -1L){
                weatherItems[i].id = getIdForDate(weatherItems[i].date)
                updateList.add(weatherItems[i])
            }
        }
        if (updateList.isNotEmpty()){
            update(updateList)
        }
    }

}