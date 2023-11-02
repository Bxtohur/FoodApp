package com.bitohur.foodapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categories(
    val name: String,
    val imageUrl: String
) : Parcelable
