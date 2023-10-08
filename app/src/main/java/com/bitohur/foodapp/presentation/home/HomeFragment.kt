package com.bitohur.foodapp.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitohur.foodapp.R
import com.bitohur.foodapp.data.dummy.DummyCategoriesDataSource
import com.bitohur.foodapp.data.dummy.DummyCategoriesDataSourceImpl
import com.bitohur.foodapp.data.dummy.DummyMenuDataSource
import com.bitohur.foodapp.data.dummy.DummyMenuDataSourceImpl
import com.bitohur.foodapp.data.local.database.AppDatabase
import com.bitohur.foodapp.data.local.database.datasource.MenuDatabaseDataSource
import com.bitohur.foodapp.data.local.datastore.UserPreferenceDataSource
import com.bitohur.foodapp.data.local.datastore.UserPreferenceDataSourceImpl
import com.bitohur.foodapp.data.local.datastore.appDataStore
import com.bitohur.foodapp.data.repository.MenuRepository
import com.bitohur.foodapp.data.repository.MenuRepositoryImpl
import com.bitohur.foodapp.databinding.FragmentHomeBinding
import com.bitohur.foodapp.model.Categories
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.presentation.detail.DetailProductActivity
import com.bitohur.foodapp.presentation.home.adapter.AdapterLayoutMode
import com.bitohur.foodapp.presentation.home.adapter.CategoriesListAdapter
import com.bitohur.foodapp.presentation.home.adapter.MenuListAdapter
import com.bitohur.foodapp.utils.GenericViewModelFactory
import com.bitohur.foodapp.utils.PreferenceDataStoreHelperImpl
import com.bitohur.foodapp.utils.proceedWhen

class FragmentHome : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val adapter: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayoutMode.LINEAR) { menu: Menu ->
            navigateToDetail(menu)
        }
    }

    private val viewModel: HomeViewModel by viewModels {
        GenericViewModelFactory.create(HomeViewModel(createMenuRepo(), createPreferenceDataSource()))
    }

    private fun createPreferenceDataSource(): UserPreferenceDataSource {
        val dataStore = this.requireContext().appDataStore
        val dataStoreHelper = PreferenceDataStoreHelperImpl(dataStore)
        return UserPreferenceDataSourceImpl(dataStoreHelper)
    }

    private fun createMenuRepo(): MenuRepository {
        val cds: DummyCategoriesDataSource = DummyCategoriesDataSourceImpl()
        val database = AppDatabase.getInstance(requireContext())
        val menuDao = database.menuDao()
        val menuDataSource = MenuDatabaseDataSource(menuDao)
        return MenuRepositoryImpl(menuDataSource, cds)
    }

    private fun navigateToDetail(item: Menu) {
        DetailProductActivity.startActivity(requireContext(), item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showListCategories(viewModel.getCategoriesData())
        setupList()
        setupSwitch()
        setSwitchAction()
        observeGridPref()
        observeProductData()
    }

    private fun observeProductData() {
        viewModel.productListLiveData.observe(viewLifecycleOwner){
            it.proceedWhen(doOnSuccess = {
                it.payload?.let { it1 -> adapter.submitData(it1) }
            })
        }
    }
    private fun observeGridPref() {
        viewModel.usingGridLiveData.observe(viewLifecycleOwner) { isUsingGrid ->
            binding.switchListGrid.isChecked = isUsingGrid
            (binding.rvMenu.layoutManager as GridLayoutManager).spanCount = if (isUsingGrid) 2 else 1
            adapter.adapterLayoutMode = if(isUsingGrid) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
            adapter.refreshList()
        }
    }
    private fun setupSwitch() {
        binding.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setUsingGridPref(isChecked)
        }
    }

    private fun setSwitchAction() {
        binding.switchListGrid.setOnCheckedChangeListener { _, isUsingGrid->
            viewModel.setUsingGridPref(isUsingGrid)
        }
    }

    private fun showListCategories(data : List<Categories>) {
        val categoryListAdapter = CategoriesListAdapter()
        binding.rvCategories.adapter = categoryListAdapter
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL, false )
        categoryListAdapter.setData(data)
    }



    private fun setupList() {
        val span = if(adapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(requireContext(),span)
            adapter = this@FragmentHome.adapter
        }
    }


}