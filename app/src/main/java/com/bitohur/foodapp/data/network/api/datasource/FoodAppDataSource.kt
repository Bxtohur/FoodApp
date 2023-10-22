package com.bitohur.foodapp.data.network.api.datasource

import com.bitohur.foodapp.data.network.api.model.category.CategoriesResponse
import com.bitohur.foodapp.data.network.api.model.order.OrderRequest
import com.bitohur.foodapp.data.network.api.model.order.OrderResponse
import com.bitohur.foodapp.data.network.api.model.menu.MenusResponse
import com.bitohur.foodapp.data.network.api.service.FoodAppApiService

interface FoodAppDataSource {
    suspend fun getProducts(category: String? = null): MenusResponse
    suspend fun getCategories(): CategoriesResponse
    suspend fun createOrder(orderRequest: OrderRequest): OrderResponse
}

class FoodAppApiDataSource(private val service: FoodAppApiService) : FoodAppDataSource{
    override suspend fun getProducts(category: String?): MenusResponse {
        return service.getProducts(category)
    }

    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse {
        return service.createOrder(orderRequest)
    }

}