package com.bitohur.foodapp.presentation.checkout

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitohur.foodapp.R
import com.bitohur.foodapp.data.dummy.DummyMenuDataSource
import com.bitohur.foodapp.data.dummy.DummyMenuDataSourceImpl
import com.bitohur.foodapp.data.local.database.AppDatabase
import com.bitohur.foodapp.data.local.database.datasource.CartDataSource
import com.bitohur.foodapp.data.local.database.datasource.CartDatabaseDataSource
import com.bitohur.foodapp.data.local.database.datasource.MenuDataSource
import com.bitohur.foodapp.data.repository.CartRepository
import com.bitohur.foodapp.data.repository.CartRepositoryImpl
import com.bitohur.foodapp.databinding.ActivityCheckoutBinding
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.presentation.common.adapter.CartListAdapter
import com.bitohur.foodapp.utils.GenericViewModelFactory
import com.bitohur.foodapp.utils.ResultWrapper
import com.bitohur.foodapp.utils.proceedWhen
import com.bitohur.foodapp.utils.toCurrencyFormat


class CheckoutActivity : AppCompatActivity() {
    private val viewModel: CheckoutViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource)
        GenericViewModelFactory.create(CheckoutViewModel(repo))
    }

    private val binding : ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        observeData()
//        showListSummary()
    }

    private fun setupList() {
        binding.layoutContent.rvCart.adapter = adapter
    }
    private fun showListSummary() {
        val summaryListAdapter = SummaryListAdapter()
        val layoutState = findViewById<View>(R.id.layout_state)
        val recyclerView = layoutState.findViewById<RecyclerView>(R.id.rv_shopping_summary)
        recyclerView.adapter = summaryListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false )
        summaryListAdapter.setData(DummyMenuDataSourceImpl().getMenus())
    }
    private fun observeData() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        viewModel.cartList.observe(this) {
            it.proceedWhen(doOnSuccess = { result ->
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = true
                binding.layoutContent.rvCart.isVisible = true
                binding.cvSectionOrder.isVisible = true
                result.payload?.let { (carts, totalPrice) ->
                    adapter.submitData(carts)
                    binding.tvTotalPrice.text = totalPrice.toCurrencyFormat()
                }
            }, doOnLoading = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = true
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            }, doOnError = { err ->
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = err.exception?.message.orEmpty()
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            }, doOnEmpty = { data ->
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                data.payload?.let { (_, totalPrice) ->
                    binding.tvTotalPrice.text = totalPrice.toCurrencyFormat()
                }
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            })
        }
    }
}