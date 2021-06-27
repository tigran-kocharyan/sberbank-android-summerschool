package ru.totowka.taskmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SingleTaskActivity : AppCompatActivity(), View.OnClickListener {
    private var isNewTask = false
    private var standard_id = 0
    private var single_instance_id = 0
    private var single_task_id = 0
    private var single_top_id = 0

    companion object {
        private const val ON_NEW_INTENT_MESSAGE =
            "Current: Single Task Activity\nIt was on Stack Top, called OnNewIntent() and deleted all activities above"
        private const val MESSAGE = "Current: Single Task Activity"
        private const val LOG_TAG = "Task manager"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        standard_id = intent.getIntExtra("standard_id", 0)
        single_instance_id = intent.getIntExtra("single_instance_id", 0)
        single_task_id = intent.getIntExtra("single_task_id", 0)
        single_top_id = intent.getIntExtra("single_top_id", 0)

        setContentView(R.layout.activity_basic)
        findViewById<Button>(R.id.standardTask).setOnClickListener(this)
        findViewById<Button>(R.id.singleTopTask).setOnClickListener(this)
        findViewById<Button>(R.id.singleTask).setOnClickListener(this)
        findViewById<Button>(R.id.singleInstance).setOnClickListener(this)
        Log.d(LOG_TAG, "Single Task Activity №$single_task_id onCreate()")
        printTask()
    }

    private fun printTask() {
        findViewById<TextView>(R.id.description).text = MESSAGE + "\nID: №$single_task_id"
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        findViewById<TextView>(R.id.description).text = ON_NEW_INTENT_MESSAGE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(LOG_TAG, "Single Task Activity №$single_task_id onSaveInstanceState()")
        outState.putString("desc", findViewById<TextView>(R.id.description).text as String)
        outState.putInt("standard_id", standard_id)
        outState.putInt("single_instance_id", single_instance_id)
        outState.putInt("single_task_id", single_task_id)
        outState.putInt("single_top_id", single_top_id)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(LOG_TAG, "Single Task Activity №$single_task_id onRestoreInstanceState()")
        findViewById<TextView>(R.id.description).text = savedInstanceState.getString("desc")
        standard_id = savedInstanceState.getInt("standard_id", 0)
        single_instance_id = savedInstanceState.getInt("single_instance_id", 0)
        standard_id = savedInstanceState.getInt("single_task_id", 0)
        standard_id = savedInstanceState.getInt("single_top_id", 0)
    }

    override fun onClick(v: View?) {
        isNewTask = findViewById<Switch>(R.id.taskChoice).isChecked
        var intent: Intent
        if (v != null) {
            when (v.id) {
                R.id.singleInstance -> {
                    intent = Intent(this, SingleInstanceActivity::class.java)
                    putData(intent, single_instance_id = this.single_instance_id + 1)
                }
                R.id.singleTask -> {
                    intent = Intent(this, SingleTaskActivity::class.java)
                    putData(intent, single_task_id = this.single_task_id + 1)
                }
                R.id.singleTopTask -> {
                    intent = Intent(this, SingleTopActivity::class.java)
                    putData(intent, single_top_id = this.single_top_id + 1)
                }
                R.id.standardTask -> {
                    intent = Intent(this, StandardActivity::class.java)
                    putData(intent, standard_id = this.standard_id + 1)
                }
                else -> return
            }
            startActivity(intent.apply {
                if (isNewTask) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            })
        }
    }

    private fun putData(
        intent: Intent,
        standard_id: Int = this.standard_id,
        single_instance_id: Int = this.single_instance_id,
        single_task_id: Int = this.single_task_id,
        single_top_id: Int = this.single_top_id
    ): Intent {
        intent.putExtra("standard_id", standard_id)
        intent.putExtra("single_instance_id", single_instance_id)
        intent.putExtra("single_task_id", single_task_id)
        intent.putExtra("single_top_id", single_top_id)
        return intent
    }
}