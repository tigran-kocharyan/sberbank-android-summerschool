package ru.totowka.voicerecorder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class RecordingsAdapter(private var recordings: Array<File>, private val callback: MainActivity.ItemCallback) :
    RecyclerView.Adapter<RecordingsAdapter.PhotoHolder>() {

    fun updaterecordings(recordings: Array<File>) {
        this.recordings = recordings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
        PhotoHolder(parent.inflate(R.layout.item_recording, false), callback)

    override fun getItemCount(): Int = recordings.size

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val recording = recordings[position]
        holder.bindFile(recording)
    }

    class PhotoHolder(private val view: View, private val callback: MainActivity.ItemCallback) :
        RecyclerView.ViewHolder(view){
        private var title: TextView = view.findViewById(R.id.textView)

        fun bindFile(file: File) {
            this.title.text = file.name
            view.setOnClickListener {
                callback.onClick(file.name)
            }
        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }
}