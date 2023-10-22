package com.bitohur.foodapp.presentation.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.bitohur.foodapp.data.repository.CartRepository
import com.bitohur.foodapp.data.repository.MenuRepository
import com.bitohur.foodapp.data.repository.UserRepository
import com.bitohur.foodapp.model.Categories
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutViewModel(private val cartRepository: CartRepository, private val userRepository: UserRepository) : ViewModel() {
    val cartList = cartRepository.getUserCartData().asLiveData(Dispatchers.IO)

    private val _checkoutResult = MutableLiveData<ResultWrapper<Boolean>>()
    val checkoutResult: LiveData<ResultWrapper<Boolean>>
        get() = _checkoutResult

    fun createOrder(){
        viewModelScope.launch(Dispatchers.IO) {
            val carts = cartList.value?.payload?.first ?: return@launch
            val total = cartList.value?.payload?.second?.toInt() ?: 0
            val username = userRepository.getCurrentUser()?.fullName.orEmpty()
            cartRepository.createOrder(carts, total, username).collect{
                _checkoutResult.postValue(it)
            }
        }
    }

    fun clearCart(){
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAll()
        }
    }
}