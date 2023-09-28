package com.bitohur.foodapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bitohur.foodapp.core.ViewHolderBinder
import com.bitohur.foodapp.databinding.ItemGridMenuBinding
import com.bitohur.foodapp.databinding.ItemListCategoriesBinding
import com.bitohur.foodapp.model.Categories
import com.bitohur.foodapp.model.Menu

class CategoriesListAdapter(): RecyclerView.Adapter<CategoriesItemListViewHolder>() {
    private val differ =  AsyncListDiffer(this,object : DiffUtil.ItemCallback<Categories>(){
        override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    })

    fun setData(data : List<Categories>){
        differ.submitList(data)
        notifyItemRangeChanged(0,data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesItemListViewHolder {
        return CategoriesItemListViewHolder(
            binding = ItemListCategoriesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CategoriesItemListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

}

class CategoriesItemListViewHolder(
    private val binding: ItemListCategoriesBinding) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Categories> {
    override fun bind(item: Categories) {
        binding.tvCategoriesName.text = item.categories
        binding.ivIconCategories.load(item.imgUrl)
    }
}