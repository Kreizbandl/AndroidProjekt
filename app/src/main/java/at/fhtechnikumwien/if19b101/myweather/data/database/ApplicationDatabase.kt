package at.fhtechnikumwien.if19b101.myweather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import at.fhtechnikumwien.if19b101.myweather.data.WeatherItem

@Database(entities = [WeatherItem::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun weatherItemDao(): WeatherItemDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            val tmpInstance = INSTANCE
            if(tmpInstance != null){
                return tmpInstance
            }
            synchronized(this){
                val tmpInstance2 = INSTANCE
                if(tmpInstance2 != null){
                    return tmpInstance2
                }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationDatabase::class.java,
                    "weather_item"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}