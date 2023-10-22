package com.bitohur.foodapp.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.bitohur.foodapp.data.local.database.AppDatabase
import com.bitohur.foodapp.data.local.database.datasource.CartDataSource
import com.bitohur.foodapp.data.local.database.datasource.CartDatabaseDataSource
import com.bitohur.foodapp.data.network.api.datasource.FoodAppApiDataSource
import com.bitohur.foodapp.data.network.api.service.FoodAppApiService
import com.bitohur.foodapp.data.repository.CartRepository
import com.bitohur.foodapp.data.repository.CartRepositoryImpl
import com.bitohur.foodapp.databinding.ActivityDetailMenuBinding
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.utils.GenericViewModelFactory
import com.bitohur.foodapp.utils.proceedWhen
import com.bitohur.foodapp.utils.toCurrencyFormat

class DetailProductActivity : AppCompatActivity() {

    private val binding: ActivityDetailMenuBinding by lazy {
        ActivityDetailMenuBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailProductViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val service = FoodAppApiService.invoke()
        val dataSource = FoodAppApiDataSource(service)
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource, dataSource)
        GenericViewModelFactory.create(DetailProductViewModel(intent?.extras,repo, this ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindProduct(viewModel.product)
        observeData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.ibBtnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnMinus.setOnClickListener{
            viewModel.minus()
        }
        binding.btnPlus.setOnClickListener{
            viewModel.add()
        }
        binding.btnAddToCart.setOnClickListener {
            viewModel.addToCart()
        }
        binding.llLocation.setOnClickListener {
            viewModel.navigateToMaps()
        }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this){
            binding.tvPriceTotal.text = it.toCurrencyFormat()
        }
        viewModel.productCountLiveData.observe(this){
            binding.tvCounter.text = it.toString()
        }
        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                }, doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun bindProduct(product: Menu?) {
        product?.let { item ->
            binding.ivBannerDetail.load(item.imageUrl) {
                crossfade(true)
            }
            binding.tvMenuName.text = item.name
            binding.tvDetailPrice.text = item.price.toCurrencyFormat()
            binding.tvLocation.text = item.location
            binding.tvDetailDesc.apply {
                text = item.desc
                movementMethod = ScrollingMovementMethod()
            }
            binding.tvPriceTotal.text = item.price.toCurrencyFormat()
        }
    }

    companion object {
        const val EXTRA_PRODUCT = "EXTRA_PRODUCT"
        fun startActivity(context: Context, product: Menu) {
            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT, product)
            context.startActivity(intent)
        }
    }
}
