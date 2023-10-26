package com.bitohur.foodapp.di

import android.content.Context
import com.bitohur.foodapp.data.local.database.AppDatabase
import com.bitohur.foodapp.data.local.database.datasource.CartDataSource
import com.bitohur.foodapp.data.local.database.datasource.CartDatabaseDataSource
import com.bitohur.foodapp.data.local.datastore.UserPreferenceDataSource
import com.bitohur.foodapp.data.local.datastore.UserPreferenceDataSourceImpl
import com.bitohur.foodapp.data.local.datastore.appDataStore
import com.bitohur.foodapp.data.network.api.datasource.FoodAppApiDataSource
import com.bitohur.foodapp.data.network.api.datasource.FoodAppDataSource
import com.bitohur.foodapp.data.network.api.service.FoodAppApiService
import com.bitohur.foodapp.data.repository.CartRepository
import com.bitohur.foodapp.data.repository.CartRepositoryImpl
import com.bitohur.foodapp.data.repository.MenuRepository
import com.bitohur.foodapp.data.repository.MenuRepositoryImpl
import com.bitohur.foodapp.presentation.cart.CartViewModel
import com.bitohur.foodapp.presentation.home.HomeViewModel
import com.bitohur.foodapp.utils.GenericViewModelFactory
import com.bitohur.foodapp.utils.PreferenceDataStoreHelperImpl

class AppInjection(private val context: Context) {
    val service = FoodAppApiService.invoke()
    val dataSource = FoodAppApiDataSource(service)
    val dataStore = context.appDataStore
    val dataStoreHelper = PreferenceDataStoreHelperImpl(dataStore)
    val userPref: UserPreferenceDataSource = UserPreferenceDataSourceImpl(dataStoreHelper)
    val homeRepo: MenuRepository = MenuRepositoryImpl(dataSource)


    fun getCartRepositoryFactory(): CartRepository {
        val database = AppDatabase.getInstance(context)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val cartRepo: CartRepository = CartRepositoryImpl(cartDataSource, dataSource)
        return cartRepo
    }

    fun getHomeViewModelFactory() = GenericViewModelFactory.create(HomeViewModel(homeRepo, userPref))
    fun getCartViewModelFactory() = GenericViewModelFactory.create(CartViewModel(getCartRepositoryFactory()))
}