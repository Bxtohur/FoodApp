package com.bitohur.foodapp.presentation.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bitohur.foodapp.core.ViewHolderBinder
import com.bitohur.foodapp.databinding.ItemCostBinding
import com.bitohur.foodapp.databinding.ItemListCategoriesBinding
import com.bitohur.foodapp.model.Categories
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.presentation.home.adapter.CategoriesItemListViewHolder
import com.bitohur.foodapp.utils.toCurrencyFormat


class SummaryListAdapter() : RecyclerView.Adapter<SummaryListViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.price == newItem.price &&
                    oldItem.menuImgUrl == newItem.menuImgUrl &&
                    oldItem.desc == newItem.desc
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    })

    fun setData(data: List<Menu>) {
        differ.submitList(data)
        notifyItemRangeChanged(0, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryListViewHolder {
        return SummaryListViewHolder(
            binding = ItemCostBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: SummaryListViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

}

class SummaryListViewHolder(
    private val binding: ItemCostBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        binding.tvItemTitle.text = item.name
        binding.tvItemPrice.text = item.price.toCurrencyFormat()
    }
}