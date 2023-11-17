package com.bitohur.foodapp.data.repository

import app.cash.turbine.test
import com.bitohur.foodapp.data.local.database.datasource.CartDataSource
import com.bitohur.foodapp.data.local.database.entity.CartEntity
import com.bitohur.foodapp.data.network.api.datasource.FoodAppDataSource
import com.bitohur.foodapp.data.network.api.model.order.OrderResponse
import com.bitohur.foodapp.model.Cart
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {

    @MockK
    lateinit var localDataSource: CartDataSource

    @MockK
    lateinit var remoteDataSource: FoodAppDataSource

    private lateinit var repository: CartRepository

    val fakeCartList = listOf(
        CartEntity(
            id = 1,
            productId = "1",
            productName = "Sate Cirebon",
            productPrice = 12000,
            productImgUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        ),
        CartEntity(
            id = 2,
            productId = "1",
            productName = "Sate Padang",
            productPrice = 14000,
            productImgUrl = "url",
            itemQuantity = 2,
            itemNotes = "notes"
        )
    )

    val mockCart = Cart(
        id = 1,
        productId = "1",
        productName = "Sate",
        productPrice = 12000,
        productImgUrl = "url",
        itemQuantity = 2,
        itemNotes = "notes"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CartRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun deleteAll() {
        coEvery { localDataSource.deleteAll() } returns Unit
        runTest {
            val result = repository.deleteAll()
            coVerify { localDataSource.deleteAll() }
            assertEquals(result, Unit)
        }
    }

    @Test
    fun `get user card data, result success`() {
        every { localDataSource.getAllCarts() } returns flow {
            emit(fakeCartList)
        }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(data.payload?.first?.size, 2)
                assertEquals(data.payload?.second, 52000)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data , result loading`() {
        val fakeCartList = listOf<CartEntity>()
        val flowMock = flow {
            emit(fakeCartList)
        }
        every { localDataSource.getAllCarts() } returns flowMock
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2101)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user cart data , result error`() {
        every { localDataSource.getAllCarts() } returns flow { throw IllegalStateException("Mock Error") }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `get user card data , result empty`() {
        val fakeCartList = listOf<CartEntity>()
        val flowMock = flow {
            emit(fakeCartList)
        }
        every { localDataSource.getAllCarts() } returns flowMock
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                verify { localDataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun `create card loading, product id not null`() {
        runTest {
            val mockProduct = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockProduct, 1).map {
                delay(100)
                it
            }.test {
                delay(110)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Loading)
                coVerify { localDataSource.insertCart(any()) }
            }
        }
    }

    @Test
    fun `create card success, product id not null`() {
        runTest {
            val mockProduct = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockProduct, 1).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Success)
                assertEquals(result.payload, true)
                coVerify { localDataSource.insertCart(any()) }
            }
        }
    }

    @Test
    fun `create cart error, product id not null`() {
        runTest {
            val mockProduct = mockk<Menu>(relaxed = true)
            coEvery { localDataSource.insertCart(any()) } throws IllegalStateException("Mock error")
            repository.createCart(mockProduct, 1).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Error)
                coVerify { localDataSource.insertCart(any()) }
            }
        }
    }
/*
    @Test
    fun `create cart error, product id null`() {
        runTest {
            val mockProduct = mockk<Menu>(relaxed = true) {
                every { id } returns null
            }
            coEvery { localDataSource.insertCart(any()) } returns 1
            repository.createCart(mockProduct, 1).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Error)
                assertEquals(result.exception?.message, "Product ID not found")
                coVerify(atLeast = 0) { localDataSource.insertCart(any()) }
            }
        }
    }
 */

    @Test
    fun `decrease cart when quantity less then or equal 0`() {
        coEvery { localDataSource.deleteCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `decrease cart when quantity more then or equal 0`() {
        coEvery { localDataSource.deleteCart(any()) } returns 1
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 0) { localDataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `increase cart`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `set cart notes`() {
        coEvery { localDataSource.updateCart(any()) } returns 1
        runTest {
            repository.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun `delete cart`() {
        coEvery { localDataSource.deleteCart(any()) } returns 1
        runTest {
            repository.deleteCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertEquals(result.payload, true)
                coVerify(atLeast = 1) { localDataSource.deleteCart(any()) }
            }
        }
    }

    @Test
    fun `test order`() {
        runTest {
            val mockCarts = listOf(
                Cart(
                    id = 1,
                    productId = "1",
                    productName = "Sate",
                    productPrice = 12000,
                    productImgUrl = "url",
                    itemQuantity = 2,
                    itemNotes = "notes"
                ),
                Cart(
                    id = 1,
                    productId = "1",
                    productName = "Sate",
                    productPrice = 12000,
                    productImgUrl = "url",
                    itemQuantity = 2,
                    itemNotes = "notes"
                )
            )

            coEvery { remoteDataSource.createOrder(any()) } returns OrderResponse(
                code = 200,
                message = "success",
                status = true
            )
            repository.createOrder(mockCarts, 12000, "rizal").map {
                delay(100)
                it
            }.test {
                delay(210)
                val result = expectMostRecentItem()
                assertTrue(result is ResultWrapper.Success)
            }
        }
    }
}
