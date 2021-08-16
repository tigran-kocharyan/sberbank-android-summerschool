package ru.totowka.filemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.filemanager.databinding.FileItemBinding
import java.io.File

class FileManagerAdapter() : RecyclerView.Adapter<FileManagerAdapter.ViewHolder>() {

    private var files: List<String> = emptyList()

    fun updateFiles(rootFile: File) {
        files = listFiles(rootFile)
        notifyDataSetChanged()
    }

    fun updateFiles(rootDocumentFile: DocumentFile) {
        files = rootDocumentFile.listFiles().filter { it.isDirectory }.mapNotNull { it.name }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.file_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(files[position])
    }

    override fun getItemCount() = files.size

    private fun listFiles(rootFile: File): List<String> =
        rootFile.listFiles()?.filter { it.isDirectory }?.map { it.absolutePath } ?: emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = FileItemBinding.bind(itemView)

        fun bindView(filePath: String) {
            binding.labelView.text = filePath
            itemView.setOnClickListener {
                val intent = FolderListActivity.newIntent(itemView.context, filePath)
                itemView.context.startActivity(intent)
            }
        }
    }
}