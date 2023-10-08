package com.bitohur.foodapp.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.bitohur.foodapp.data.local.database.datasource.MenuDataSource
import com.bitohur.foodapp.data.repository.CartRepository
import com.bitohur.foodapp.data.repository.MenuRepository
import com.bitohur.foodapp.model.Categories
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers

class CheckoutViewModel(private val cartRepository: CartRepository) : ViewModel() {
    val cartList = cartRepository.getUserCartData().asLiveData(Dispatchers.IO)

}