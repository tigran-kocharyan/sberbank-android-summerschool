package ru.totowka.filemanager

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ru.totowka.filemanager.databinding.ActivityMainBinding
import java.io.File

class FolderListActivity : AppCompatActivity() {
    private lateinit var pathFile: File
    private lateinit var pathDocumentFile: DocumentFile
    private lateinit var adapter: FileManagerAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val path = intent.getStringExtra(PATH_EXTRA)
        initRecycler()
        listFiles(path)
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(this@FolderListActivity)
        adapter = FileManagerAdapter()
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                layoutManager.orientation
            )
        )
    }

    private fun listFiles(path: String?) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            pathDocumentFile = path?.let { DocumentFile.fromFile(File(it))} ?: throw IllegalArgumentException("Error")
            binding.path.text = pathDocumentFile.name
            pathDocumentFile.let {
                adapter.updateFiles(it)
            }
        } else {
            pathFile = path?.let {File(it)} ?: throw IllegalArgumentException("Error")
            binding.path.text = pathFile.absolutePath
            pathFile.let { adapter.updateFiles(it) }
        }
    }

    companion object {
        private const val PATH_EXTRA = "path"

        fun newIntent(context: Context, pathFile: String) =
            Intent(context, FolderListActivity::class.java).apply {
                putExtra(PATH_EXTRA, pathFile)
            }
    }
}