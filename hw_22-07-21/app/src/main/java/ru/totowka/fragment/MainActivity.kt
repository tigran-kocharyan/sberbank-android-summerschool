package ru.totowka.fragment

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView

class MainActivity : AppCompatActivity(), FirstFragment.FirstFragmentCallback, SecondFragment.SecondFragmentCallback {

    companion object {
        private const val FIRST_FRAGMENT_TAG = "first"
        private const val SECOND_FRAGMENT_TAG = "second"
        private const val THIRD_FRAGMENT_TAG = "third"
        private const val TEXT_TAG = "text"
        private const val TEXT_DEFAULT_VALUE = "text"
    }

    private var thirdFragmentContainer: FragmentContainerView? = null
    private var text: String = ""
    private lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        root = findViewById(R.id.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_first, FirstFragment.newInstance(), FIRST_FRAGMENT_TAG)
                .add(R.id.fragment_second, SecondFragment.newInstance(), SECOND_FRAGMENT_TAG)
                .commit()
        } else {
            text = savedInstanceState.getString(TEXT_TAG, TEXT_DEFAULT_VALUE)
            if(supportFragmentManager.findFragmentByTag(THIRD_FRAGMENT_TAG) != null) {
                createThirdFragmentContainer()
                thirdFragmentContainer?.let {
                    supportFragmentManager.beginTransaction()
                        .add(it.id, ThirdFragment.newInstance(text), THIRD_FRAGMENT_TAG).commit()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_TAG, text)
    }

    override fun buttonClicked() {
        val thirdFragment = supportFragmentManager.findFragmentByTag(THIRD_FRAGMENT_TAG)
        if (thirdFragment == null) {
            createThirdFragmentContainer()
            thirdFragmentContainer?.let {
                supportFragmentManager.beginTransaction()
                    .add(it.id, ThirdFragment.newInstance(text), THIRD_FRAGMENT_TAG).commit()
            }
        } else {
            (thirdFragment as ThirdFragment).setText(text)
        }
    }

    private fun createThirdFragmentContainer() {
        if (thirdFragmentContainer == null) {
            thirdFragmentContainer = FragmentContainerView(this).apply {
                this.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
                )
                this.id = View.generateViewId()
            }
            root.addView(thirdFragmentContainer)
        }
    }

    override fun textEdited(text: String) {
        this.text = text
    }
}