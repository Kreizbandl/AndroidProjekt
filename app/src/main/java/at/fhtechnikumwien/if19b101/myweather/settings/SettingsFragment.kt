package at.fhtechnikumwien.if19b101.myweather.settings

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import at.fhtechnikumwien.if19b101.myweather.R

class SettingsFragment : PreferenceFragmentCompat() {

    //https://www.youtube.com/watch?v=4OLs8CeagdA

    private var prefLocation:EditTextPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?){
        addPreferencesFromResource(R.xml.pref_general)

        //get the correct preference and set the summary with corresponding data
        prefLocation = findPreference(getString(R.string.settings_location_key))
        prefLocation?.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()

    }
}