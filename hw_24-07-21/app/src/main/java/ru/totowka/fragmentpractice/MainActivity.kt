package ru.totowka.fragmentpractice

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var buttonAdd: Button
    private lateinit var buttonDelete: Button
    private lateinit var radioAdd: RadioButton
    private lateinit var radioReplace: RadioButton
    private lateinit var checkboxAddToBackstack: CheckBox
    private var counter = 0

    companion object {
        private const val COUNTER_KEY = "COUNTER_KEY"
        private const val COUNTER_DEFAULT = 0
        private const val TAG = "MainActivityFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState != null) {
            counter = savedInstanceState.getInt(COUNTER_KEY, COUNTER_DEFAULT)
        }

        buttonAdd = findViewById(R.id.button_add)
        buttonDelete = findViewById(R.id.button_delete)
        radioAdd = findViewById(R.id.radio_add)
        radioReplace = findViewById(R.id.radio_replace)
        checkboxAddToBackstack = findViewById(R.id.checkbox_add_to_backstack)

        radioAdd.isChecked = true
        buttonAdd.setOnClickListener { add() }
        buttonDelete.setOnClickListener { delete() }
    }

    private fun add() {
        val count = supportFragmentManager.backStackEntryCount
        when (isRadioAdd()) {
            true -> {
                if (isAddToBackstack()) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_holder, TextFragment.newInstance("Fragment №$count"), "Fragment $count")
                        .addToBackStack("Fragment $count")
                        .commit()

                } else {
                    supportFragmentManager.beginTransaction()
                        .add(
                            R.id.fragment_holder,
                            TextFragment.newInstance("Fragment №$counter not in BackStack"),
                            "Fragment №$counter not in BackStack"
                        )
                        .commit()
                    counter++
                }
            }
            false -> {
                if (isAddToBackstack()) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, TextFragment.newInstance("Fragment №$count"), "Fragment $count")
                        .addToBackStack("Fragment $count")
                        .commit()

                } else {
                    counter = 0
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_holder,
                            TextFragment.newInstance("Fragment №$counter not in BackStack"),
                            "Fragment №$counter not in BackStack"
                        )
                        .commit()
                    counter++
                }
            }
        }

    }

    private fun delete() {
        val index = supportFragmentManager.backStackEntryCount - 1
        if (index >= 0) {
            val backEntry = supportFragmentManager.getBackStackEntryAt(index)
            val fragment = supportFragmentManager.findFragmentByTag(backEntry.name)
            Log.d(TAG, "delete() called with $index and ${backEntry.name}")
            if (fragment != null) {
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            }
        }
    }

    private fun isAddToBackstack(): Boolean {
        return checkboxAddToBackstack.isChecked
    }

    private fun isRadioAdd(): Boolean {
        return radioAdd.isChecked
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(COUNTER_KEY, counter)
    }
}