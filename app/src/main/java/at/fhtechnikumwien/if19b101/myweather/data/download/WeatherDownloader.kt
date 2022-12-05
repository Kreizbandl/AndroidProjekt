package at.fhtechnikumwien.if19b101.myweather.data.download

import android.util.Log
import at.fhtechnikumwien.if19b101.myweather.data.parser.JSONParser
import at.fhtechnikumwien.if19b101.myweather.data.WeatherItem
import at.fhtechnikumwien.if19b101.myweather.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class WeatherDownloader {

    //download function on different thread
    suspend fun load(urlString : String) : List<WeatherItem>? {
        return withContext(Dispatchers.IO){
            loadInt(urlString)
        }
    }

    //actual HTTP connection
    private fun loadInt(urlString: String) : List<WeatherItem>? {
        var con : HttpURLConnection? = null
        try {
            val url = URL(urlString)
            con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"

            val scanner = Scanner(con.inputStream)
            scanner.useDelimiter("\\A")
            if(scanner.hasNext()){
                //convert HTTP response via JSONParser to List of WeatherItems
                val jsonParser = JSONParser()
                return jsonParser.parseResponse(scanner.next())
            }else{
                return null
            }
        }catch(ex : MalformedURLException) {
            Log.e(MainActivity.LOG_TAG, "Malformed URL.", ex)
            return null
        } catch(ex : IOException) {
            Log.e(MainActivity.LOG_TAG, "I/O exception", ex)
            return null
        } finally {
            con?.disconnect()
        }
    }

}