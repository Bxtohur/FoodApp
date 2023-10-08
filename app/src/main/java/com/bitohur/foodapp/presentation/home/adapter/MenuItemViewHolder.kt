package com.bitohur.foodapp.presentation.home.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bitohur.foodapp.core.ViewHolderBinder
import com.bitohur.foodapp.databinding.ItemGridMenuBinding
import com.bitohur.foodapp.databinding.ItemLinearMenuBinding
import com.bitohur.foodapp.model.Menu

private fun Double.formatCurrency(currencySymbol: String): String {
    val formattedAmount = String.format("% ,.0f", this).replace("," , ".")
    return "$currencySymbol $formattedAmount"
}

class LinearMenuItemViewHolder(
    private val binding: ItemLinearMenuBinding,
    private val onClickListener: (Menu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        binding.ivMenu.load(item.menuImgUrl) {
            crossfade(true)
        }
        binding.tvMenuTitle.text = item.name
        binding.tvMenuPrice.text = item.price.formatCurrency("Rp.")
        binding.tvMenuDesc.text = item.desc
        binding.root.setOnClickListener {
            onClickListener.invoke(item)
        }
    }
}

class GridMenuItemViewHolder(
    private val binding: ItemGridMenuBinding,
    private val onClickListener: (Menu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        binding.ivMenu.load(item.menuImgUrl) {
            crossfade(true)
        }
        binding.tvMenuTitle.text = item.name
        binding.tvMenuPrice.text = item.price.formatCurrency("Rp.")
        binding.root.setOnClickListener {
            onClickListener.invoke(item)
        }
    }
}