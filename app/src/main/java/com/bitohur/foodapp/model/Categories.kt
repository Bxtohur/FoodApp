package com.bitohur.foodapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Categories(
    val name: String,
    val imageUrl: String
): Parcelable
