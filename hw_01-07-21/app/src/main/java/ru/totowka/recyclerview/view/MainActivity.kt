package ru.totowka.recyclerview.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.recyclerview.R
import ru.totowka.recyclerview.adapter.StorageAdapter
import ru.totowka.recyclerview.adapter.StorageItemTouchHelper
import ru.totowka.recyclerview.adapter.StorageItemTouchHelperCallback
import ru.totowka.recyclerview.controller.StorageRepository
import ru.totowka.recyclerview.model.type.Apple
import ru.totowka.recyclerview.model.type.Basket
import ru.totowka.recyclerview.model.type.StorageItem
import ru.totowka.recyclerview.model.util.GreedException


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var storageRepository: StorageRepository
    private lateinit var storageAdapter: StorageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var storage: ArrayList<StorageItem>
    private var listener = object : StorageViewClickListener {
        override fun onClick(view: View?, position: Int) {
            try {
                storage = storageRepository.onClick(storage, position)
                storageAdapter.reload(storage)
            } catch (exception: GreedException) {
                if (view != null) {
                    Toast.makeText(
                        view.context,
                        "No more than 3 apples in a basket!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private var itemTouchHelper = object : StorageItemTouchHelper {
        override fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean {
            return try {
                storage = storageRepository.onMove(storage, fromPosition, toPosition)
                storageAdapter.reload(storage)
                true
            } catch (exception: GreedException) {
                Toast.makeText(
                    applicationContext,
                    "No more than 3 apples in a basket!",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        }

        override fun onItemDismiss(position: Int) {
            storage = storageRepository.onSwipe(storage, position)
            storageAdapter.reload(storage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storageRepository = StorageRepository()
        recyclerView = findViewById(R.id.recyclerView)
        findViewById<Button>(R.id.button_add_basket).setOnClickListener(this)
        findViewById<Button>(R.id.button_delete_baskets).setOnClickListener(this)

        val basket1 = Basket("Корзина 1", arrayListOf(Apple()))
        val basket2 = Basket("Корзина 2", arrayListOf(Apple(), Apple()))
        val basket3 = Basket("Корзина 3", arrayListOf(Apple(), Apple(), Apple()))
        val baskets = listOf(basket1, basket2, basket3)
        this.storage = storageRepository.store(baskets)

        storageAdapter = StorageAdapter(storage, listener, itemTouchHelper)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = storageAdapter

        val callback: ItemTouchHelper.Callback = StorageItemTouchHelperCallback(storageAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.button_add_basket -> {
                storage = storageRepository.addBasket(storage)
                storageAdapter.reload(storage)
            }
            R.id.button_delete_baskets -> {
                storage = storageRepository.deleteAll()
                storageAdapter.reload(storage)
            }
        }
    }
}