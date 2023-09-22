package com.bitohur.foodapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID


@Parcelize
data class Menu(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val price: Double,
    val desc: String,
    val imgUrl: String,
    val location: String
): Parcelable
