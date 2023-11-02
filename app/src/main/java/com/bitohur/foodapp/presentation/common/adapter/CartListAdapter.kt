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
import com.bitohur.foodapp.utils.doneEditing
import com.bitohur.foodapp.utils.toCurrencyFormat

class CartListAdapter(private val cartListener: CartListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Cart>() {
                override fun areItemsTheSame(
                    oldItem: Cart,
                    newItem: Cart
                ): Boolean {
                    return oldItem.id == newItem.id && oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Cart,
                    newItem: Cart
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    fun submitData(data: List<Cart>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (cartListener != null) {
            CartViewHolder(
                ItemCartMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                cartListener
            )
        } else {
            CartOrderViewHolder(
                ItemCartMenuOrderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }
}
class CartViewHolder(
    private val binding: ItemCartMenuBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {

    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
        setClickListener(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            ivProductImage.load(item.productImgUrl) {
                crossfade(true)
            }
            tvProductName.text = item.productName
            tvProductPrice.text = (item.productPrice).toCurrencyFormat()
            tvProductCount.text = item.itemQuantity.toString()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.etNotesItem.setText(item.itemNotes)
        binding.etNotesItem.doneEditing {
            binding.etNotesItem.clearFocus()
            val newItem = item.copy().apply {
                itemNotes = binding.etNotesItem.text.toString().trim()
            }
            cartListener?.onUserDoneEditingNotes(newItem)
        }
    }

    private fun setClickListener(item: Cart) {
        with(binding) {
            ivMinus.setOnClickListener {
                cartListener?.onMinusTotalItemCartClicked(item)
            }
            ivPlus.setOnClickListener {
                cartListener?.onPlusTotalItemCartClicked(item)
            }
            ivRemoveCart.setOnClickListener {
                cartListener?.onRemoveCartClicked(item)
            }
        }
    }
}
class CartOrderViewHolder(
    private val binding: ItemCartMenuOrderBinding
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {

    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            ivProductImage.load(item.productImgUrl) {
                crossfade(true)
            }
            tvTotalQuantity.text = itemView.rootView.context.getString(
                R.string.total_quantity,
                item.itemQuantity.toString()
            )
            tvProductName.text = item.productName
            tvProductPrice.text = (item.productPrice).toCurrencyFormat()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.tvNotes.text = item.itemNotes
    }
}

interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}
