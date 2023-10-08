package com.bitohur.foodapp.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bitohur.foodapp.R
import com.bitohur.foodapp.core.ViewHolderBinder
import com.bitohur.foodapp.databinding.ItemCartMenuBinding
import com.bitohur.foodapp.databinding.ItemCartMenuOrderBinding
import com.bitohur.foodapp.model.Cart
import com.bitohur.foodapp.model.CartMenu
import com.bitohur.foodapp.utils.doneEditing
import com.bitohur.foodapp.utils.toCurrencyFormat

class CartListAdapter(private val cartListener: CartListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<CartMenu>() {
            override fun areItemsTheSame(
                oldItem: CartMenu,
                newItem: CartMenu
            ): Boolean {
                return oldItem.cart.id == newItem.cart.id && oldItem.menu.id == newItem.menu.id
            }

            override fun areContentsTheSame(
                oldItem: CartMenu,
                newItem: CartMenu
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        })

    fun submitData(data: List<CartMenu>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (cartListener != null) CartViewHolder(
            ItemCartMenuBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), cartListener
        ) else CartOrderViewHolder(
            ItemCartMenuOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<CartMenu>).bind(dataDiffer.currentList[position])
    }

}

class CartViewHolder(
    private val binding: ItemCartMenuBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<CartMenu> {
    override fun bind(item: CartMenu) {
        setCartData(item)
        setCartNotes(item)
        setClickListeners(item)
    }

    private fun setCartData(item: CartMenu) {
        with(binding) {
            binding.ivProductImage.load(item.menu.menuImgUrl) {
                crossfade(true)
            }
            tvProductCount.text = item.cart.itemQuantity.toString()
            tvProductName.text = item.menu.name
            tvProductPrice.text = (item.cart.itemQuantity * item.menu.price).toCurrencyFormat()
        }
    }

    private fun setCartNotes(item: CartMenu) {
        binding.etNotesItem.setText(item.cart.itemNotes)
        binding.etNotesItem.doneEditing {
            binding.etNotesItem.clearFocus()
            val newItem = item.cart.copy().apply {
                itemNotes = binding.etNotesItem.text.toString().trim()
            }
            cartListener?.onUserDoneEditingNotes(newItem)
        }
    }

    private fun setClickListeners(item: CartMenu) {
        with(binding) {
            ivMinus.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item.cart) }
            ivPlus.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item.cart) }
            ivRemoveCart.setOnClickListener { cartListener?.onRemoveCartClicked(item.cart) }
        }
    }
}

class CartOrderViewHolder(
    private val binding: ItemCartMenuOrderBinding,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<CartMenu> {
    override fun bind(item: CartMenu) {
        setCartData(item)
        setCartNotes(item)
    }

    private fun setCartData(item: CartMenu) {
        with(binding) {
            binding.ivProductImage.load(item.menu.menuImgUrl) {
                crossfade(true)
            }
            tvTotalQuantity.text =
                itemView.rootView.context.getString(
                    R.string.total_quantity,
                    item.cart.itemQuantity.toString()
                )
            tvProductName.text = item.menu.name
            tvProductPrice.text = (item.cart.itemQuantity * item.menu.price).toCurrencyFormat()
        }
    }

    private fun setCartNotes(item: CartMenu) {
        binding.tvNotes.text = item.cart.itemNotes
    }

}


interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}