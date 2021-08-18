package ru.totowka.mvvm.presentation.view.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.totowka.mvvm.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    companion object {
        const val TAG = "SettingsFragment"

        /**
         * Фабричный метод для создания экземпляра [SettingsFragment]
         *
         * @return экземпрял [SettingsFragment]
         */
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}