package com.bitohur.foodapp.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.bitohur.foodapp.data.repository.UserRepository

class SplashViewModel(private val repository: UserRepository) : ViewModel() {

    fun isUserLoggedIn() = repository.isLoggedIn()
}
