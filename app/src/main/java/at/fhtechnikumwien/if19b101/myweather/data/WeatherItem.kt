package at.fhtechnikumwien.if19b101.myweather.data

import androidx.annotation.NonNull
import androidx.room.*
import java.io.Serializable
import java.util.*

@Entity(tableName = "weather_item", indices = [Index(value = ["date"], unique = true)]) //tableName for a different name, for performance and unique entries for date (no twins)
data class WeatherItem(
    @ColumnInfo(name = "_id") @PrimaryKey(autoGenerate = true) var id : Long?, //@ColumnInfo() for a different name
    var icon : String,
    var condition : String,
    var date : String,
    var temperature : Double,
    var pressure : String,
    var humidity : String,
    var cloudCover : String,
    var windSpeed : Double,
    var windDirection : String,
    var rain : String,
    var snow : String ) : Serializable {
    constructor(icon: String, condition: String, date: String, temperature: Double, pressure: String,
                humidity: String, cloudCover: String, windSpeed: Double, windDirection: String, rain: String, snow: String)
            : this(null, icon, condition, date, temperature, pressure,
                humidity, cloudCover, windSpeed, windDirection, rain, snow) }