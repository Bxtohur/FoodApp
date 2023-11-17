package com.bitohur.foodapp.data.repository

import app.cash.turbine.test
import com.bitohur.foodapp.data.network.api.datasource.FoodAppDataSource
import com.bitohur.foodapp.data.network.api.model.category.CategoriesResponse
import com.bitohur.foodapp.data.network.api.model.category.CategoryResponse
import com.bitohur.foodapp.data.network.api.model.menu.MenuItemResponse
import com.bitohur.foodapp.data.network.api.model.menu.MenusResponse
import com.bitohur.foodapp.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class MenuRepositoryImplTest {
    @MockK
    lateinit var remoteDataSource: FoodAppDataSource
    private lateinit var repository: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = MenuRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `get categories, with result loading`() {
        val mockCategoryResponse = mockk<CategoriesResponse>()
        runTest {
            coEvery { remoteDataSource.getCategories() } returns mockCategoryResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result success`() {
        val fakeCategoryResponse = CategoryResponse(
            imgUrl = "url",
            name = "name"
        )
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeCategoryResponse)
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.name, "name")
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result empty`() {
        val fakeCategoriesResponse = CategoriesResponse(
            code = 200,
            status = true,
            message = "Success but empty",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getCategories() } returns fakeCategoriesResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get categories, with result error`() {
        runTest {
            coEvery { remoteDataSource.getCategories() } throws IllegalStateException("Mock error")
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getCategories() }
            }
        }
    }

    @Test
    fun `get products, with result loading`() {
        val mockProductResponse = mockk<MenusResponse>()
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns mockProductResponse
            repository.getProducts("makanan").map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get products, with result success`() {
        val fakeProductItemResponse = MenuItemResponse(
            desc = "desc",
            name = "name",
            price = 12000,
            imageUrl = "url",
            priceFormat = "12000",
            location = "lokasi"
        )
        val fakeProductsResponse = MenusResponse(
            code = 200,
            status = true,
            message = "Success",
            data = listOf(fakeProductItemResponse)
        )
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns fakeProductsResponse
            repository.getProducts("sembarang").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.size, 1)
                assertEquals(data.payload?.get(0)?.name, "name")
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get products, with result empty`() {
        val fakeProductsResponse = MenusResponse(
            code = 200,
            status = true,
            message = "Success",
            data = emptyList()
        )
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } returns fakeProductsResponse
            repository.getProducts().map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }

    @Test
    fun `get products, with result error`() {
        runTest {
            coEvery { remoteDataSource.getProducts(any()) } throws IllegalStateException("Mock error")
            repository.getProducts("bebas").map {
                delay(100)
                it
            }.test {
                delay(220)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { remoteDataSource.getProducts(any()) }
            }
        }
    }
}
