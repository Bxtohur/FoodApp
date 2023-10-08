package com.bitohur.foodapp.data.local.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bitohur.foodapp.data.local.database.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

interface MenuDao {
    @Query("SELECT * FROM MENU")
    fun getAllMenu(): Flow<List<MenuEntity>>

    @Query("SELECT * FROM MENU WHERE id == :id")
    fun getMenuById(id: Int): Flow<MenuEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(product: List<MenuEntity>)

    @Delete
    suspend fun deleteMenu(product: MenuEntity): Int

    @Update
    suspend fun updateMenu(product: MenuEntity): Int
}