package com.bitohur.foodapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Categories(
    val id: String = UUID.randomUUID().toString(),
    val categories: String,
    val imgUrl: String
): Parcelable
