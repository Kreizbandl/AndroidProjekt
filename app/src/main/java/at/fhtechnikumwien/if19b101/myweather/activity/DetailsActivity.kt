package at.fhtechnikumwien.if19b101.myweather.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import at.fhtechnikumwien.if19b101.myweather.R
import at.fhtechnikumwien.if19b101.myweather.data.WeatherItem
import kotlin.math.roundToInt

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val ITEM_KEY = "item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        //set screen title
        title = "MyWeather Details"

        val item = intent?.extras?.getSerializable(ITEM_KEY) as? WeatherItem
        if(item != null){
            //choose the right image (could be done more compact)
            var tmpWeatherIcon : Int? = null
            when (item.icon) {
                "01d" -> { tmpWeatherIcon = R.drawable._01d}
                "02d" -> { tmpWeatherIcon = R.drawable._02d}
                "03d" -> { tmpWeatherIcon = R.drawable._03d}
                "04d" -> { tmpWeatherIcon = R.drawable._04d}
                "09d" -> { tmpWeatherIcon = R.drawable._09d}
                "10d" -> { tmpWeatherIcon = R.drawable._10d}
                "11d" -> { tmpWeatherIcon = R.drawable._11d}
                "13d" -> { tmpWeatherIcon = R.drawable._13d}
                "50d" -> { tmpWeatherIcon = R.drawable._50d}
                "01n" -> { tmpWeatherIcon = R.drawable._01n}
                "02n" -> { tmpWeatherIcon = R.drawable._02n}
                "03n" -> { tmpWeatherIcon = R.drawable._03n}
                "04n" -> { tmpWeatherIcon = R.drawable._04n}
                "09n" -> { tmpWeatherIcon = R.drawable._09n}
                "10n" -> { tmpWeatherIcon = R.drawable._10n}
                "11n" -> { tmpWeatherIcon = R.drawable._11n}
                "13n" -> { tmpWeatherIcon = R.drawable._13n}
                "50n" -> { tmpWeatherIcon = R.drawable._50n}
            }
            if (tmpWeatherIcon != null) {
                findViewById<ImageView>(R.id.iv_weather_icon).setImageResource(tmpWeatherIcon)
            }

            findViewById<TextView>(R.id.tv_date).text = "Forecast for " + item.date
            findViewById<TextView>(R.id.tv_condition).text = item.condition.capitalize()
            findViewById<TextView>(R.id.tv_pressure).text = item.pressure + " hPa"
            findViewById<TextView>(R.id.tv_humidity).text = item.humidity + " %"
            findViewById<TextView>(R.id.tv_cloudCover).text = item.cloudCover + " %"
            findViewById<TextView>(R.id.tv_windDirection).text = item.windDirection + " °"
            findViewById<TextView>(R.id.tv_rain).text = item.rain + " mm"
            findViewById<TextView>(R.id.tv_snow).text = item.snow + " mm"

            //convert to other units
            //https://openweathermap.org/weather-data
            //°C && m/s -> °F && miles/h
            if(getMeasurementUnitBoolean()){
                findViewById<TextView>(R.id.tv_temperature).text = item.temperature.toString() + " °C"
                findViewById<TextView>(R.id.tv_windSpeed).text = item.windSpeed.toString() + " m/s"
            }else{
                //°C -> °F
                ((((item.temperature * 1.8 + 32) * 100.0).roundToInt() / 100.0).toString() + " °F")
                    .also {
                        findViewById<TextView>(R.id.tv_temperature).text = it
                    }
                //m/s -> miles/h
                ((((item.windSpeed * 2.237) * 100.0).roundToInt() / 100.0).toString() + "mph")
                    .also {
                        findViewById<TextView>(R.id.tv_windSpeed).text = it
                    }
            }

        }
    }

    private fun getMeasurementUnitBoolean(): Boolean{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getBoolean(
            getString(R.string.settings_measurement_unit_key),
            resources.getBoolean(R.bool.settings_measurement_unit_default_value)
        )
    }
}