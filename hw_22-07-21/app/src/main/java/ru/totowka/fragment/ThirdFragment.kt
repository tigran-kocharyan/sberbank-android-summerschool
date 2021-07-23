package ru.totowka.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ThirdFragment : Fragment() {
    companion object {
        fun newInstance(init: String): ThirdFragment {
            return ThirdFragment().apply {
                this.init = init
            }
        }
    }

    private lateinit var mTextView: TextView
    private var init: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTextView = view.findViewById(R.id.text_view)
        mTextView.text = this.init
    }

    fun setText(text: String) {
        mTextView.text = text
    }
}