package ru.totowka.transliterator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class TransliterateActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mEnToRuButton: Button
    private lateinit var mEnToRuText: TextInputEditText
    private lateinit var mRuToEnButton: Button
    private lateinit var mRuToEnText: TextInputEditText

    companion object {
        private val cyrillic = charArrayOf(' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н',
                'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
                'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н',
                'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я')
        private val latin = arrayOf(" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "i", "k", "l", "m", "n",
                "o", "p", "r", "s", "t", "u", "f", "kh", "ts", "ch", "sh", "shch", "ie", "y", "", "e", "iu", "ia",
                "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "I", "K", "L", "M", "N",
                "O", "P", "R", "S", "T", "U", "F", "Kh", "Ts", "Ch", "Sh", "Scsh", "Ie", "Y", "", "E", "Iu", "Ia")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mEnToRuButton = findViewById(R.id.english_to_russian)
        mRuToEnButton = findViewById(R.id.russian_to_english)
        mEnToRuText = findViewById(R.id.en_to_ru_text)
        mRuToEnText = findViewById(R.id.ru_to_en_text)

        mEnToRuButton.setOnClickListener(this)
        mRuToEnButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.english_to_russian -> {
                    if (mEnToRuText.text?.isBlank() == false)
                        mRuToEnText.setText(transliterateToRussian(mEnToRuText.text.toString()))
                }
                R.id.russian_to_english -> {
                    if (mRuToEnText.text?.isBlank() == false)
                        mEnToRuText.setText(transliterateToEnglish(mRuToEnText.text.toString()))
                }
            }
        }
    }

    private fun transliterateToEnglish(message: String): String {
        val builder = StringBuilder()
        for (letter in message) {
            for (x in cyrillic.indices) {
                if (letter == cyrillic[x]) {
                    builder.append(latin[x])
                    break
                }
            }
        }
        return builder.toString()
    }

    private fun transliterateToRussian(message: String): String {
        val builder = StringBuilder()
        for (letter in message) {
            for (x in latin.indices) {
                if (letter.toString() == latin[x]) {
                    builder.append(cyrillic[x])
                    break
                }
            }
        }
        return builder.toString()
    }
}