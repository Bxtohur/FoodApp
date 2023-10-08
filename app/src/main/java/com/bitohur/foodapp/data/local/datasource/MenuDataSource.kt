package com.bitohur.foodapp.data.local.datasource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bitohur.foodapp.data.local.database.dao.MenuDao
import com.bitohur.foodapp.data.local.database.entity.MenuEntity
import kotlinx.coroutines.flow.Flow

interface MenuDataSource {
    @Query("SELECT * FROM MENU")
    fun getAllMenus(): Flow<List<MenuEntity>>

    @Query("SELECT * FROM MENU WHERE id == :id")
    fun getMenuById(id: Int): Flow<MenuEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(product: List<MenuEntity>)

    @Delete
    suspend fun deleteMenu(product: MenuEntity): Int

    @Update
    suspend fun updateMenu(product: MenuEntity): Int
}

class MenuDatabaseDataSource(private val dao : MenuDao) : MenuDataSource {
    override fun getAllMenus(): Flow<List<MenuEntity>> {
        return dao.getAllMenu()
    }

    override fun getMenuById(id: Int): Flow<MenuEntity> {
        return dao.getMenuById(id)
    }

    override suspend fun insertMenu(product: List<MenuEntity>) {
        return dao.insertMenu(product)
    }

    override suspend fun deleteMenu(product: MenuEntity): Int {
        return dao.deleteMenu(product)
    }

    override suspend fun updateMenu(product: MenuEntity): Int {
        return dao.updateMenu(product)
    }
}