package com.bitohur.foodapp.data.repository

import com.bitohur.foodapp.data.network.api.datasource.FoodAppDataSource
import com.bitohur.foodapp.data.network.api.model.category.toCategoryList
import com.bitohur.foodapp.data.network.api.model.menu.toProductList
import com.bitohur.foodapp.model.Categories
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.utils.ResultWrapper
import com.bitohur.foodapp.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getCategories(): Flow<ResultWrapper<List<Categories>>>
    fun getProducts(category: String? = null): Flow<ResultWrapper<List<Menu>>>
}

class MenuRepositoryImpl(private val apiDataSource: FoodAppDataSource
) : MenuRepository {

    override fun getCategories(): Flow<ResultWrapper<List<Categories>>> {
        return proceedFlow {
            apiDataSource.getCategories().data?.toCategoryList() ?: emptyList()
        }
    }

    override fun getProducts(category: String?): Flow<ResultWrapper<List<Menu>>> {
        return proceedFlow {
            apiDataSource.getProducts(category).data?.toProductList() ?: emptyList()
        }
    }

}