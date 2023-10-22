package com.bitohur.foodapp.data.local.database.mapper

import com.bitohur.foodapp.data.local.database.entity.CartEntity
import com.bitohur.foodapp.model.Cart

fun CartEntity?.toCart() = Cart(
    id = this?.id,
    productId = this?.productId.orEmpty(),
    productName = this?.productName.orEmpty(),
    productPrice = this?.productPrice ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    productImgUrl = this?.productImgUrl.orEmpty(),
    itemNotes = this?.itemNotes.orEmpty()
)

fun Cart?.toCartEntity() = CartEntity(
    id = this?.id,
    productId = this?.productId.orEmpty(),
    productName = this?.productName.orEmpty(),
    productPrice = this?.productPrice ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    productImgUrl = this?.productImgUrl.orEmpty(),
    itemNotes = this?.itemNotes.orEmpty()
)

fun List<CartEntity?>.toCartList() = this.map { it.toCart() }
