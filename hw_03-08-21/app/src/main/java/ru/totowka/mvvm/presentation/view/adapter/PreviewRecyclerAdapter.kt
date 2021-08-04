package ru.totowka.mvvm.presentation.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ru.totowka.mvvm.R
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.databinding.ViewRecyclerItemBinding
import ru.totowka.mvvm.presentation.view.DogFactActivity

/**
 * Адаптер для отображения списка DogImageModel
 *
 * @param list список с DogImageModel
 */
class PreviewRecyclerAdapter(private val list: List<DogImageModel>) :
    RecyclerView.Adapter<PreviewRecyclerAdapter.PreviewViewHolder>() {

    companion object {
        private const val DOG_INFO_URL = "doginfourl"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val inflated = parent.inflate(R.layout.view_recycler_item, false)
        return PreviewViewHolder(inflated)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount() = list.size

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(this.context).inflate(layoutRes, this, attachToRoot)
    }

    class PreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mBinding: ViewRecyclerItemBinding = ViewRecyclerItemBinding.bind(itemView)

        fun bindView(dogImageModel: DogImageModel) {
            mBinding.title.text = "Dog Fact #${adapterPosition+1}"
            Glide.with(itemView.context)
                .load(dogImageModel.imgUrl)
                .error(R.drawable.ic_baseline_broken_image_24)
                .placeholder(R.drawable.ic_baseline_circle_24)
                .circleCrop()
                .into(mBinding.image)
            itemView.setOnClickListener {
                val context = itemView.context
                val showPhotoIntent = Intent(context, DogFactActivity::class.java)
                showPhotoIntent.putExtra(DOG_INFO_URL, dogImageModel.imgUrl)
                context.startActivity(showPhotoIntent)
                Snackbar.make(it, "Clicked!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}