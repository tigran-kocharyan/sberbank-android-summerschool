package ru.totowka.fragmentpractice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TextFragment : Fragment() {
    companion object {
        private final val COUNTER_KEY = "counter"

        fun newInstance(text: String): TextFragment {
            return TextFragment().apply {
                this.arguments = Bundle().apply {
                    this.putString(COUNTER_KEY, text)
                }
            }
        }
    }

    private lateinit var textView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_textview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(R.id.text)
        textView.text = requireArguments().getString(COUNTER_KEY)
    }
}