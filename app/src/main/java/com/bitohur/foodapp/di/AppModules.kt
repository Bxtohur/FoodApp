package com.bitohur.foodapp.di

import com.bitohur.foodapp.data.local.database.AppDatabase
import com.bitohur.foodapp.data.local.database.datasource.CartDataSource
import com.bitohur.foodapp.data.local.database.datasource.CartDatabaseDataSource
import com.bitohur.foodapp.data.local.datastore.UserPreferenceDataSource
import com.bitohur.foodapp.data.local.datastore.UserPreferenceDataSourceImpl
import com.bitohur.foodapp.data.local.datastore.appDataStore
import com.bitohur.foodapp.data.network.api.datasource.FoodAppApiDataSource
import com.bitohur.foodapp.data.network.api.datasource.FoodAppDataSource
import com.bitohur.foodapp.data.network.api.service.FoodAppApiService
import com.bitohur.foodapp.data.network.firebase.auth.FirebaseAuthDataSource
import com.bitohur.foodapp.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.bitohur.foodapp.data.repository.CartRepository
import com.bitohur.foodapp.data.repository.CartRepositoryImpl
import com.bitohur.foodapp.data.repository.MenuRepository
import com.bitohur.foodapp.data.repository.MenuRepositoryImpl
import com.bitohur.foodapp.data.repository.UserRepository
import com.bitohur.foodapp.data.repository.UserRepositoryImpl
import com.bitohur.foodapp.presentation.cart.CartViewModel
import com.bitohur.foodapp.presentation.checkout.CheckoutViewModel
import com.bitohur.foodapp.presentation.detail.DetailProductViewModel
import com.bitohur.foodapp.presentation.home.HomeViewModel
import com.bitohur.foodapp.presentation.login.LoginViewModel
import com.bitohur.foodapp.presentation.profile.ProfileViewModel
import com.bitohur.foodapp.presentation.register.RegisterViewModel
import com.bitohur.foodapp.presentation.splashscreen.SplashViewModel
import com.bitohur.foodapp.utils.AssetWrapper
import com.bitohur.foodapp.utils.PreferenceDataStoreHelper
import com.bitohur.foodapp.utils.PreferenceDataStoreHelperImpl
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single { androidContext().appDataStore }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
        single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }
    }

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { FoodAppApiService.invoke(get()) }
        single { FirebaseAuth.getInstance() }
    }

    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<FoodAppDataSource> { FoodAppApiDataSource(get()) }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
    }

    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
        single<MenuRepository> { MenuRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModelOf(::HomeViewModel)
        viewModel { params -> DetailProductViewModel(params.get(), get(), androidContext()) }
        viewModelOf(::CartViewModel)
        viewModelOf(::CheckoutViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::SplashViewModel)
    }

    private val utilsModule = module {
        single { AssetWrapper(androidContext()) }
    }

    val modules: List<Module> = listOf(
        localModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule,
        utilsModule
    )
}
