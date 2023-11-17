package com.bitohur.foodapp.data.network.api.datasource

import com.bitohur.foodapp.data.network.api.model.category.CategoriesResponse
import com.bitohur.foodapp.data.network.api.model.menu.MenusResponse
import com.bitohur.foodapp.data.network.api.model.order.OrderRequest
import com.bitohur.foodapp.data.network.api.model.order.OrderResponse
import com.bitohur.foodapp.data.network.api.service.FoodAppApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FoodAppApiDataSourceTest {

    @MockK
    lateinit var service: FoodAppApiService
    private lateinit var dataSource: FoodAppDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FoodAppApiDataSource(service)
    }

    @Test
    fun getProducts() {
        runTest {
            val mockResponse = mockk<MenusResponse>()
            coEvery { service.getProducts(any()) } returns mockResponse
            val response = dataSource.getProducts("makanan")
            coVerify { service.getProducts(any()) }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val response = dataSource.getCategories()
            coVerify { service.getCategories() }
            assertEquals(response, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockResponse = mockk<OrderResponse>(relaxed = true)
            val mockRequest = mockk<OrderRequest>(relaxed = true)
            coEvery { service.createOrder(any()) } returns mockResponse
            val response = dataSource.createOrder(mockRequest)
            coVerify { service.createOrder(any()) }
            assertEquals(response, mockResponse)
        }
    }
}
