package com.bitohur.foodapp.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class MenuEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "desc")
    val desc: String,
    @ColumnInfo(name = "menu_img_url")
    val menuImgUrl: String,
    @ColumnInfo(name = "location")
    val location: String
)
