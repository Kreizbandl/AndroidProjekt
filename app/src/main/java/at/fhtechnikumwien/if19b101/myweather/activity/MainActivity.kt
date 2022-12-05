package at.fhtechnikumwien.if19b101.myweather.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhtechnikumwien.if19b101.myweather.R
import at.fhtechnikumwien.if19b101.myweather.adapter.ListAdapter
import at.fhtechnikumwien.if19b101.myweather.data.WeatherRepository
import at.fhtechnikumwien.if19b101.myweather.settings.SettingsActivity
import at.fhtechnikumwien.if19b101.myweather.viewmodels.WeatherListViewModel
import at.fhtechnikumwien.if19b101.myweather.viewmodels.WeatherListViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        val LOG_TAG = MainActivity::class.simpleName
    }

    private val viewModel by lazy { ViewModelProvider(this, WeatherListViewModelFactory(application, WeatherRepository(applicationContext)))[WeatherListViewModel::class.java] }
    private var adapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter = ListAdapter(measurementUnit = getMeasurementUnitBoolean())
        recyclerView.adapter = adapter
        //creates list item separation decoration line
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        //navigate to details activity on item click and pass data
        adapter?.itemClickHandler = {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.ITEM_KEY, it)
            startActivity(intent)
        }
        //observe the viewModel weather items and update listAdapter
        viewModel.weatherItems.observe(this){
            //if db is empty, use standard background operation / thread
            if(it.isEmpty()){
                Toast.makeText(this,"DB empty. Reload weather data for the first time.",Toast.LENGTH_SHORT).show()
                viewModel.reload(delete = true)
            }
            adapter?.items = it
        }
    }

    //create the 3 dots with the menu items
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    //define what should happen when selecting a menu point
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            //forward to settings activity
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            //re-download data
            R.id.action_reload -> {
                Toast.makeText(this, "Reload weather data.", Toast.LENGTH_SHORT).show()
                viewModel.reload(delete = false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //helper function to get the settings measurement unit
    private fun getMeasurementUnitBoolean(): Boolean{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getBoolean(
            getString(R.string.settings_measurement_unit_key),
            resources.getBoolean(R.bool.settings_measurement_unit_default_value)
        )
    }

    //react to settings changes
    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        if(p1 == getString(R.string.settings_measurement_unit_key)){
            //reload the listAdapter with different measurement units but without re-downloading
            adapter?.reload(getMeasurementUnitBoolean())
        } else if (p1 == getString(R.string.settings_location_key)){
            Toast.makeText(this, "Reload weather data.", Toast.LENGTH_SHORT).show()
            //actually re-download data from web
            viewModel.reload(delete = true)
        }
    }
}