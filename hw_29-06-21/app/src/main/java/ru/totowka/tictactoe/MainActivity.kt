package ru.totowka.tictactoe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.totowka.tictactoe.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var button11: Button
    private lateinit var button12: Button
    private lateinit var button13: Button
    private lateinit var button21: Button
    private lateinit var button22: Button
    private lateinit var button23: Button
    private lateinit var button31: Button
    private lateinit var button32: Button
    private lateinit var button33: Button
    private lateinit var controller: Button
    private lateinit var indicatorAI: ImageView
    private lateinit var indicatorAICount: TextView
    private lateinit var indicatorPlayer: ImageView
    private lateinit var indicatorPlayerCount: TextView
    private var countPlayer = 0
    private var countAI = 0
    private var cells = 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
        setListeners()
        setDisabled()
    }

    private fun initialize() {
        button11 = binding.button11
        button12 = binding.button12
        button13 = binding.button13
        button21 = binding.button21
        button22 = binding.button22
        button23 = binding.button23
        button31 = binding.button31
        button32 = binding.button32
        button33 = binding.button33
        controller = binding.controller
        indicatorAI = binding.indicatorAI
        indicatorAICount = binding.indicatorAICount
        indicatorPlayer = binding.indicatorPlayer
        indicatorPlayerCount = binding.indicatorPlayerCount
    }

    private fun setListeners() {
        button11.setOnClickListener(this)
        button12.setOnClickListener(this)
        button13.setOnClickListener(this)
        button21.setOnClickListener(this)
        button22.setOnClickListener(this)
        button23.setOnClickListener(this)
        button31.setOnClickListener(this)
        button32.setOnClickListener(this)
        button33.setOnClickListener(this)
        controller.setOnClickListener(this)
    }

    private fun setEnabled() {
        button11.isEnabled = true
        button12.isEnabled = true
        button13.isEnabled = true
        button21.isEnabled = true
        button22.isEnabled = true
        button23.isEnabled = true
        button31.isEnabled = true
        button32.isEnabled = true
        button33.isEnabled = true
    }

    private fun setDisabled() {
        button11.isEnabled = false
        button12.isEnabled = false
        button13.isEnabled = false
        button21.isEnabled = false
        button22.isEnabled = false
        button23.isEnabled = false
        button31.isEnabled = false
        button32.isEnabled = false
        button33.isEnabled = false
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.button11 -> {
                    button11.background.level = 2
                    button11.text = "P"
                    button11.isEnabled = false
                    checkWin()
                }
                R.id.button12 -> {
                    button12.background.level = 2
                    button12.text = "P"
                    button12.isEnabled = false
                    checkWin()
                }
                R.id.button13 -> {
                    button13.background.level = 2
                    button13.text = "P"
                    button13.isEnabled = false
                    checkWin()
                }
                R.id.button21 -> {
                    button21.background.level = 2
                    button21.text = "P"
                    button21.isEnabled = false
                    checkWin()
                }
                R.id.button22 -> {
                    button22.background.level = 2
                    button22.isEnabled = false
                    button22.text = "P"
                    checkWin()
                }
                R.id.button23 -> {
                    button23.background.level = 2
                    button23.text = "P"
                    button23.isEnabled = false
                    checkWin()
                }
                R.id.button31 -> {
                    button31.background.level = 2
                    button31.isEnabled = false
                    button31.text = "P"
                    checkWin()
                }
                R.id.button32 -> {
                    button32.text = "P"
                    button32.background.level = 2
                    button32.isEnabled = false
                    checkWin()
                }
                R.id.button33 -> {
                    button33.text = "P"
                    button33.background.level = 2
                    button33.isEnabled = false
                    checkWin()
                }
                R.id.controller -> {
                    setEnabled()
                    restart()
                    countPlayer = 0
                    countAI = 0
                    indicatorPlayerCount.text = "Score: $countPlayer"
                    indicatorAICount.text = "Score: $countAI"
                    indicatorAI.setImageLevel(0)
                    indicatorPlayer.setImageLevel(0)
                    controller.isEnabled = false
                }
            }
        }
    }

    private fun restart() {
        button11.background.level = 0
        button11.text = ""
        button12.background.level = 0
        button12.text = ""
        button13.background.level = 0
        button13.text = ""
        button21.background.level = 0
        button21.text = ""
        button22.background.level = 0
        button22.text = ""
        button23.background.level = 0
        button23.text = ""
        button31.background.level = 0
        button31.text = ""
        button32.background.level = 0
        button32.text = ""
        button33.background.level = 0
        button33.text = ""
        cells = 9
        setEnabled()
    }

    @SuppressLint("SetTextI18n")
    private fun checkWin() {
        cells--
        if (checkWinPlayer()) {
            countPlayer++
            indicatorPlayer.setImageLevel(countPlayer * 3333)
            indicatorPlayerCount.text = "Score: $countPlayer"
            restart()
        }

        chooseRandom()
        if (checkWinAI()) {
            countAI++
            indicatorAI.setImageLevel(countAI * 3333)
            indicatorAICount.text = "Score: $countAI"
            restart()
        }
        if(countPlayer == 3) {
            Toast.makeText(this, "PLAYER WIN", Toast.LENGTH_LONG).show()
            controller.isEnabled = true
            setDisabled()
        }
        if(countAI == 3) {
            Toast.makeText(this, "AI WIN", Toast.LENGTH_LONG).show()
            controller.isEnabled = true
            setDisabled()
        }
        if(checkDraw() || cells == 0) {
            Log.d("123", "DRAW")
            restart()
        }
    }

    private fun checkDraw() : Boolean{
        if(!button11.isEnabled && !button21.isEnabled && !button31.isEnabled &&
            !button12.isEnabled && !button22.isEnabled && !button32.isEnabled &&
            !button13.isEnabled && !button23.isEnabled && !button33.isEnabled) {
            return true
        }
        return false
    }

    private fun checkWinPlayer(): Boolean {
        if ((button11.text == "P" && button12.text == "P" && button13.text == "P")
            || (button21.text == "P" && button22.text == "P" && button23.text == "P")
            || (button31.text == "P" && button32.text == "P" && button33.text == "P")
            || (button11.text == "P" && button21.text == "P" && button31.text == "P")
            || (button12.text == "P" && button22.text == "P" && button32.text == "P")
            || (button13.text == "P" && button23.text == "P" && button33.text == "P")
            || (button11.text == "P" && button22.text == "P" && button33.text == "P")
            || (button13.text == "P" && button22.text == "P" && button31.text == "P")
        ) {
            return true
        }
        return false
    }

    private fun checkWinAI(): Boolean {
        if ((button11.text == "C" && button12.text == "C" && button13.text == "C")
            || (button21.text == "C" && button22.text == "C" && button23.text == "C")
            || (button31.text == "C" && button32.text == "C" && button33.text == "C")
            || (button11.text == "C" && button21.text == "C" && button31.text == "C")
            || (button12.text == "C" && button22.text == "C" && button32.text == "C")
            || (button13.text == "C" && button23.text == "C" && button33.text == "C")
            || (button11.text == "C" && button22.text == "C" && button33.text == "C")
            || (button13.text == "C" && button22.text == "C" && button31.text == "C")
        ) {
            return true
        }
        return false
    }

    private fun chooseRandom() {
        while(true) {
            when (Random.nextInt(0,9)) {
                0 -> {
                    if (button11.isEnabled) {
                        button11.text = "C"
                        button11.background.level = 1
                        button11.isEnabled = false
                        return
                    }
                }
                1 -> {
                    if (button12.isEnabled) {
                        button12.text = "C"
                        button12.background.level = 1
                        button12.isEnabled = false
                        return
                    }
                }
                2 -> {
                    if (button13.isEnabled) {
                        button13.text = "C"
                        button13.background.level = 1
                        button13.isEnabled = false
                        return
                    }
                }
                3 -> {
                    if (button21.isEnabled) {
                        button21.text = "C"
                        button21.background.level = 1
                        button21.isEnabled = false
                        return
                    }
                }
                4 -> {
                    if (button22.isEnabled) {
                        button22.text = "C"
                        button22.background.level = 1
                        button22.isEnabled = false
                        return
                    }
                }
                5 -> {
                    if (button23.isEnabled) {
                        button23.text = "C"
                        button23.background.level = 1
                        button23.isEnabled = false
                        return
                    }
                }
                6 -> {
                    if (button31.isEnabled) {
                        button31.text = "C"
                        button31.background.level = 1
                        button31.isEnabled = false
                        return
                    }
                }
                7 -> {
                    if (button32.isEnabled) {
                        button32.text = "C"
                        button32.background.level = 1
                        button32.isEnabled = false
                        return
                    }
                }
                8 -> {
                    if (button33.isEnabled) {
                        button33.text = "C"
                        button33.background.level = 1
                        button33.isEnabled = false
                        return
                    }
                }
            }
        }
        cells--

    }
}