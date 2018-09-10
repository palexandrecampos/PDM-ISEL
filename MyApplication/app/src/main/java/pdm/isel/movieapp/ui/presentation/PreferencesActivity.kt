package pdm.isel.movieapp.ui.presentation

import android.app.Fragment
import android.os.Bundle
import android.preference.PreferenceFragment
import pdm.isel.movieapp.R


class PreferencesActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        var fragment: Fragment = SettingsScreen()
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.constraint_layout, fragment, "app_preferences")
            fragmentTransaction.commit()
        } else {
            fragment = fragmentManager.findFragmentByTag("app_preferences")
        }
    }

    class SettingsScreen : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.app_preferences)
        }
    }
}
