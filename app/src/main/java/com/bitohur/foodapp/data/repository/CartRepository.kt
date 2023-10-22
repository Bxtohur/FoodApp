package com.bitohur.foodapp.data.repository

import com.bitohur.foodapp.data.local.database.entity.CartEntity
import com.bitohur.foodapp.data.local.database.mapper.toCartEntity
import com.bitohur.foodapp.data.local.database.datasource.CartDataSource
import com.bitohur.foodapp.data.local.database.mapper.toCartList
import com.bitohur.foodapp.data.network.api.datasource.FoodAppDataSource
import com.bitohur.foodapp.data.network.api.model.order.OrderItemRequest
import com.bitohur.foodapp.data.network.api.model.order.OrderRequest
import com.bitohur.foodapp.model.Cart
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.model.toOrderItemRequestList
import com.bitohur.foodapp.utils.ResultWrapper
import com.bitohur.foodapp.utils.proceed
import com.bitohur.foodapp.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.lang.IllegalStateException

interface CartRepository {
    fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Int>>>
    suspend fun createCart(product: Menu, totalQuantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAll()
    suspend fun createOrder(items: List<Cart>, totalPrice: Int, username: String): Flow<ResultWrapper<Boolean>>
}

class CartRepositoryImpl(
    private val dataSource: CartDataSource,
    private val apiDataSource: FoodAppDataSource,
) : CartRepository {

    override fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Int>>> {
        return dataSource.getAllCarts()
            .map {
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf {
                        val pricePerItem = it.productPrice
                        val quantity = it.itemQuantity
                        pricePerItem * quantity
                    }
                    Pair(result, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == true)
                    ResultWrapper.Empty(it.payload)
                else
                    it
            }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    //gunakan di detail
    override suspend fun createCart(
        product: Menu,
        totalQuantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return product.id?.let { productId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(
                        productId = productId,
                        itemQuantity = totalQuantity,
                        productImgUrl = product.imageUrl,
                        productName = product.name,
                        productPrice = product.price,
                    )
                )
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Product ID not found")))
        }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }

    override suspend fun createOrder(items: List<Cart>, totalPrice: Int, username: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItem = items.toOrderItemRequestList()
            val orderRequest = OrderRequest(orderItem, totalPrice, username)
            apiDataSource.createOrder(orderRequest).status == true
        }
    }

}