package com.bitohur.foodapp.data.network.api.model.category

import androidx.annotation.Keep
import com.bitohur.foodapp.model.Categories
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryResponse(
    @SerializedName("image_url")
    val imgUrl: String?,
    @SerializedName("nama")
    val name: String?
)

fun CategoryResponse.toCategory() = Categories(
    imageUrl = this.imgUrl.orEmpty(),
    name = this.name.orEmpty()
)

fun Collection<CategoryResponse>.toCategoryList() = this.map {
    it.toCategory()
}