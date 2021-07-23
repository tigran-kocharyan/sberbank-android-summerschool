package ru.totowka.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class SecondFragment() : Fragment() {

    companion object {
        fun newInstance() : SecondFragment {
            return SecondFragment()
        }
    }

    private lateinit var mButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mButton = view.findViewById(R.id.button)
        mButton.setOnClickListener {
            (requireActivity() as SecondFragmentCallback).buttonClicked()
        }
    }

    interface SecondFragmentCallback {
        fun buttonClicked()
    }
}