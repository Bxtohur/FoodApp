package com.bitohur.foodapp.data.local.database.mapper

import com.bitohur.foodapp.data.local.database.entity.CartEntity
import com.bitohur.foodapp.data.local.database.relation.CartMenuRelation
import com.bitohur.foodapp.model.Cart
import com.bitohur.foodapp.model.CartMenu

fun CartEntity?.toCart() = Cart(
    id = this?.id ?: 0,
    productId = this?.productId ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    itemNotes = this?.itemNotes.orEmpty()
)

fun Cart?.toCartEntity() = CartEntity(
    id = this?.id,
    productId = this?.productId ?: 0,
    itemQuantity = this?.itemQuantity ?: 0,
    itemNotes = this?.itemNotes.orEmpty()
)

fun List<CartEntity?>.toCartList() = this.map { it.toCart() }

fun CartMenuRelation.toCartMenu() = CartMenu(
    cart = this.cart.toCart(),
    menu = this.menu.toMenu()
)

fun List<CartMenuRelation>.toCartMenuList() = this.map { it.toCartMenu() }