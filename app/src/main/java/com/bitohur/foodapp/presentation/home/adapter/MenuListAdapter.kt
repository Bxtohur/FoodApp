package com.bitohur.foodapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bitohur.foodapp.core.ViewHolderBinder
import com.bitohur.foodapp.databinding.ItemGridMenuBinding
import com.bitohur.foodapp.databinding.ItemLinearMenuBinding
import com.bitohur.foodapp.model.Menu

class MenuListAdapter(
    var adapterLayoutMode: AdapterLayoutMode,
    private val onClickListener: (Menu) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    private val dataDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Menu>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            AdapterLayoutMode.GRID.ordinal -> {
                GridMenuItemViewHolder(
                    binding = ItemGridMenuBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onClickListener
                )
            }

            else -> {
                LinearMenuItemViewHolder(
                    binding = ItemLinearMenuBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onClickListener
                )
            }

        }

    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Menu>).bind(dataDiffer.currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return  adapterLayoutMode.ordinal
    }

    fun submitData(data: List<Menu>){
        dataDiffer.submitList(data)
    }
    fun refreshList(){
        notifyItemRangeChanged(0, dataDiffer.currentList.size)
    }
}