package com.bitohur.foodapp.data.network.api.model.menu

import androidx.annotation.Keep
import com.bitohur.foodapp.model.Menu
import com.google.gson.annotations.SerializedName
@Keep
data class MenuItemResponse(
    @SerializedName("detail")
    val desc: String?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("harga")
    val price: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("harga_format")
    val priceFormat: String?,
    @SerializedName("alamat_resto")
    val location: String?
)

fun MenuItemResponse.toProduct() = Menu(
    name = this.name.orEmpty(),
    price = this.price ?: 0,
    desc = this.desc.orEmpty(),
    imageUrl = this.imageUrl.orEmpty(),
    location = this.location.orEmpty(),
    priceFormat = this.priceFormat.orEmpty()

)

fun Collection<MenuItemResponse>.toProductList() = this.map { it.toProduct() }