package com.bitohur.foodapp.data.local.database.mapper

import com.bitohur.foodapp.data.local.database.entity.MenuEntity
import com.bitohur.foodapp.model.Menu

fun MenuEntity?.toMenu() = Menu(
    id = this?.id ?: 0,
    name = this?.name.orEmpty(),
    price = this?.price ?: 0.0,
    desc = this?.desc.orEmpty(),
    menuImgUrl = this?.menuImgUrl.orEmpty(),
    location = this?.location.orEmpty()
)

fun Menu?.toMenuEntity() = MenuEntity(
    name = this?.name.orEmpty(),
    price = this?.price ?: 0.0,
    desc = this?.desc.orEmpty(),
    menuImgUrl = this?.menuImgUrl.orEmpty(),
    location = this?.location.orEmpty()
).apply {
    this@toMenuEntity?.id?.let {
        this.id = this@toMenuEntity.id
    }
}

fun List<MenuEntity?>.toMenuList() = this.map { it.toMenu() }
fun List<Menu?>.toMenuEntity() = this.map { it.toMenuEntity() }