package com.bitohur.foodapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID


@Parcelize
data class Menu(
    val id: String = UUID.randomUUID().toString(),
    val imageUrl: String,
    val name: String,
    val priceFormat: String,
    val price: Int,
    val desc: String,
    val location: String
): Parcelable

