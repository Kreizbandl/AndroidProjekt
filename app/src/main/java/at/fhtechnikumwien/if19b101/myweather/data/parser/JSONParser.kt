package at.fhtechnikumwien.if19b101.myweather.data.parser

import android.util.Log
import at.fhtechnikumwien.if19b101.myweather.data.WeatherItem
import at.fhtechnikumwien.if19b101.myweather.activity.MainActivity
import org.json.JSONObject

class JSONParser {

    //https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2

    fun parseResponse(response : String) : List<WeatherItem> {
        val jsonRoot = JSONObject(response)
        val list = jsonRoot.optJSONArray("list")
        val data = mutableSetOf<WeatherItem>()

        if (list == null)
            Log.w(MainActivity.LOG_TAG, "No list found.")
        else {
            for (i in 0 until list.length()) {
                val entry = list.getJSONObject(i)
                Log.d(MainActivity.LOG_TAG, "entry $i: $entry")

                //Fetch all data for single entry / timestamp
                //ICON
                val weather = entry.getJSONArray("weather")
                val weatherObj = weather.getJSONObject(0)
                val icon = weatherObj.getString("icon")
                //CONDITION
                val condition = weatherObj.getString("description")
                //DATE and TIME
                val date = entry.getString("dt_txt")
                /*var testDate : Date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date)
                Log.d("DateTest","$testDate")*/
                //TEMPERATURE
                val main = entry.getJSONObject("main")
                val temperature : Double = main.getDouble("temp")
                //PRESSURE
                val pressure = main.getString("pressure")
                //HUMIDITY
                val humidity = main.getString("humidity")
                //CLOUD COVER
                val clouds = entry.getJSONObject("clouds")
                val cloudCover = clouds.getString("all")
                //WIND SPEED
                val wind = entry.getJSONObject("wind")
                val speed = wind.getDouble("speed")
                //WIND DIRECTION
                val windDirection = wind.getString("deg")
                //RAIN
                var rain = "0.00"
                //check if attribute exists
                if(entry.has("rain")){
                    val tmpRain = entry.getJSONObject("rain")
                    rain = tmpRain.getString("3h")
                }
                //SNOW
                var snow = "0.00"
                //check if attribute exists
                if(entry.has("snow")){
                    val tmpSnow = entry.getJSONObject("snow")
                     snow = tmpSnow.getString("3h")
                }

                //generate single object for each entry
                val singleWeatherItem = WeatherItem(
                    icon, condition, date, temperature, pressure, humidity,
                    cloudCover, speed, windDirection, rain, snow)

                data.add(singleWeatherItem)
            }
        }
        return data.toList()
    }
}