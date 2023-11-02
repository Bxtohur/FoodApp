package com.bitohur.foodapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bitohur.foodapp.databinding.ItemListCategoriesBinding
import com.bitohur.foodapp.model.Categories

class CategoriesListAdapter(private val itemClick: (Categories) -> Unit) :
    RecyclerView.Adapter<CategoriesListAdapter.ItemCategoryViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Categories>() {
                override fun areItemsTheSame(
                    oldItem: Categories,
                    newItem: Categories
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: Categories,
                    newItem: Categories
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    fun submitData(data: List<Categories>) {
        dataDiffer.submitList(data)
    }

    fun setData(data: List<Categories>) {
        dataDiffer.submitList(data)
        notifyItemChanged(0, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        val binding =
            ItemListCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemCategoryViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.bindView(dataDiffer.currentList[position])
    }

    class ItemCategoryViewHolder(
        private val binding: ItemListCategoriesBinding,
        val itemClick: (Categories) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: Categories) {
            with(item) {
                binding.ivIconCategories.load(item.imageUrl) {
                    crossfade(true)
                }
                binding.tvCategoriesName.text = item.name
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}

class MenuItemCategoriesViewHolder(
    private val binding: ItemListCategoriesBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Categories) {
        binding.ivIconCategories.load(item.imageUrl)
        binding.tvCategoriesName.text = item.name
    }
}
