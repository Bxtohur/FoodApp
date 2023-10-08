package com.bitohur.foodapp.data.repository

import com.bitohur.foodapp.data.dummy.DummyCategoriesDataSource
import com.bitohur.foodapp.data.local.database.mapper.toMenuList
import com.bitohur.foodapp.data.local.database.datasource.MenuDataSource
import com.bitohur.foodapp.model.Categories
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.utils.ResultWrapper
import com.bitohur.foodapp.utils.proceed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface MenuRepository {
    fun getCategories(): List<Categories>
    fun getMenus(): Flow<ResultWrapper<List<Menu>>>
}

class MenuRepositoryImpl(
    private val menuDataSource: MenuDataSource,
    private val dummyCategoryDataSource: DummyCategoriesDataSource
) : MenuRepository {

    override fun getCategories(): List<Categories> {
        return dummyCategoryDataSource.getCategories()
    }

    override fun getMenus(): Flow<ResultWrapper<List<Menu>>> {
        return menuDataSource.getAllMenus().map {
            proceed { it.toMenuList() }
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

}