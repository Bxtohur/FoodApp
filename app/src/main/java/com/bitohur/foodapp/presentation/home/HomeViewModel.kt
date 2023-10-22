package com.bitohur.foodapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bitohur.foodapp.data.local.datastore.UserPreferenceDataSource
import com.bitohur.foodapp.data.repository.MenuRepository
import com.bitohur.foodapp.model.Categories
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MenuRepository,
    private val userPreferenceDataSource: UserPreferenceDataSource
) : ViewModel() {

    fun setUsingGridPref(getUsingGrid : Boolean) {
        viewModelScope.launch { userPreferenceDataSource.setUsingGridPref(getUsingGrid) }
    }
    private val _categories = MutableLiveData<ResultWrapper<List<Categories>>>()
    val categories : LiveData<ResultWrapper<List<Categories>>>
        get() = _categories

    private val _products = MutableLiveData<ResultWrapper<List<Menu>>>()
    val products : LiveData<ResultWrapper<List<Menu>>>
        get() = _products

    fun getCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories().collect{
                _categories.postValue(it)
            }
        }
    }

    fun getProducts(category: String? = null){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProducts(if(category == "all") null else category?.toLowerCase()).collect{
                _products.postValue(it)
            }
        }
    }

    val usingGridLiveData = userPreferenceDataSource.getUsingGridPrefFlow().asLiveData(
        Dispatchers.IO)
}