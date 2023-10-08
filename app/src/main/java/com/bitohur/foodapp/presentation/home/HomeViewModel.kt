package com.bitohur.foodapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bitohur.foodapp.data.local.datastore.UserPreferenceDataSource
import com.bitohur.foodapp.data.repository.MenuRepository
import com.bitohur.foodapp.model.Categories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: MenuRepository,
                    private val userPreferenceDataSource: UserPreferenceDataSource): ViewModel() {
    fun setUsingGridPref(isUsingGrid: Boolean) {
        viewModelScope.launch {
            userPreferenceDataSource.setUsingGridPref(isUsingGrid)
        }
    }

    fun getCategoriesData(): List<Categories> {
        return repo.getCategories()
    }

    val usingGridLiveData = userPreferenceDataSource.isUsingGridPrefFlow().asLiveData(
        Dispatchers.IO)
    val productListLiveData = repo.getMenus().asLiveData(
        Dispatchers.IO)
}