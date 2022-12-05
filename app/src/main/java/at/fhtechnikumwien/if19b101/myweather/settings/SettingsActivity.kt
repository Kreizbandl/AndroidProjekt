package at.fhtechnikumwien.if19b101.myweather.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.fhtechnikumwien.if19b101.myweather.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //set screen title
        title = "MyWeather Settings"
    }
}