package com.bitohur.foodapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bitohur.foodapp.databinding.FragmentHomeBinding
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.presentation.detail.DetailProductActivity
import com.bitohur.foodapp.presentation.home.adapter.AdapterLayoutMode
import com.bitohur.foodapp.presentation.home.adapter.CategoriesListAdapter
import com.bitohur.foodapp.presentation.home.adapter.MenuListAdapter
import com.bitohur.foodapp.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModel()

    private val categoryAdapter: CategoriesListAdapter by lazy {
        CategoriesListAdapter {
            viewModel.getProducts(it.name)
        }
    }

    private val productAdapter: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayoutMode.LINEAR) { product: Menu ->
            navigateToDetail(product)
        }
    }

    private fun navigateToDetail(item: Menu) {
        DetailProductActivity.startActivity(requireContext(), item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        getData()
        setupList()
        setupSwitch()
        setSwitchAction()
        observeGridPref()
        observeData()
    }

    private fun getData() {
        viewModel.getCategories()
        viewModel.getProducts()
    }

    private fun observeData() {
        viewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutStateCategory.root.isVisible = false
                binding.layoutStateCategory.pbLoading.isVisible = false
                binding.layoutStateCategory.tvError.isVisible = false
                binding.rvCategories.apply {
                    isVisible = true
                    adapter = categoryAdapter
                }
                it.payload?.let { data -> categoryAdapter.submitData(data) }
            }, doOnLoading = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = true
                    binding.layoutStateCategory.tvError.isVisible = false
                    binding.rvCategories.isVisible = false
                }, doOnError = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutStateCategory.tvError.isVisible = true
                    binding.layoutStateCategory.tvError.text = it.exception?.message.orEmpty()
                    binding.rvCategories.isVisible = false
                })
        }
        viewModel.products.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutStateProduct.root.isVisible = false
                binding.layoutStateProduct.pbLoading.isVisible = false
                binding.layoutStateProduct.tvError.isVisible = false
                binding.rvMenu.apply {
                    isVisible = true
                    adapter = productAdapter
                }
                it.payload?.let { data -> productAdapter.submitData(data) }
            }, doOnLoading = {
                    binding.layoutStateProduct.root.isVisible = true
                    binding.layoutStateProduct.pbLoading.isVisible = true
                    binding.layoutStateProduct.tvError.isVisible = false
                    binding.rvMenu.isVisible = false
                }, doOnError = {
                    binding.layoutStateProduct.root.isVisible = true
                    binding.layoutStateProduct.pbLoading.isVisible = false
                    binding.layoutStateProduct.tvError.isVisible = true
                    binding.layoutStateProduct.tvError.text = it.exception?.message.orEmpty()
                    binding.rvMenu.isVisible = false
                }, doOnEmpty = {
                    binding.layoutStateProduct.root.isVisible = true
                    binding.layoutStateProduct.pbLoading.isVisible = false
                    binding.layoutStateProduct.tvError.isVisible = true
                    binding.layoutStateProduct.tvError.text = "Product not found"
                    binding.rvMenu.isVisible = false
                })
        }
    }

    private fun observeGridPref() {
        viewModel.usingGridLiveData.observe(viewLifecycleOwner) { isUsingGrid ->
            binding.switchListGrid.isChecked = isUsingGrid
            (binding.rvMenu.layoutManager as GridLayoutManager).spanCount =
                if (isUsingGrid) 2 else 1
            productAdapter.adapterLayoutMode =
                if (isUsingGrid) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
            productAdapter.refreshList()
        }
    }

    private fun setSwitchAction() {
        binding.switchListGrid.setOnCheckedChangeListener { _, isUsingGrid ->
            viewModel.setUsingGridPref(isUsingGrid)
        }
    }

    private fun setupList() {
        val span = if (productAdapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(requireContext(), span)
            adapter = this@HomeFragment.productAdapter
        }
    }

    private fun setupSwitch() {
        binding.switchListGrid.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setUsingGridPref(isChecked)
        }
    }
}
