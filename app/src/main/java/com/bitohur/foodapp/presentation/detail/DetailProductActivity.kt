package com.bitohur.foodapp.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import coil.load
import com.bitohur.foodapp.databinding.ActivityDetailMenuBinding
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.utils.proceedWhen
import com.bitohur.foodapp.utils.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailProductActivity : AppCompatActivity() {

    private val binding: ActivityDetailMenuBinding by lazy {
        ActivityDetailMenuBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailProductViewModel by viewModel {
        parametersOf(intent.extras ?: bundleOf())
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
        binding.btnMinus.setOnClickListener {
            viewModel.minus()
        }
        binding.btnPlus.setOnClickListener {
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
        viewModel.priceLiveData.observe(this) {
            binding.tvPriceTotal.text = it.toCurrencyFormat()
        }
        viewModel.productCountLiveData.observe(this) {
            binding.tvCounter.text = it.toString()
        }
        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                }
            )
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
