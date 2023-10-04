package com.bitohur.foodapp.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.viewModels
import coil.load
import com.bitohur.foodapp.R
import com.bitohur.foodapp.databinding.ActivityDetailMenuBinding
import com.bitohur.foodapp.model.Menu
import com.catnip.egroceries.utils.GenericViewModelFactory
import com.catnip.egroceries.utils.toCurrencyFormat

class DetailProductActivity : AppCompatActivity() {

    private val binding: ActivityDetailMenuBinding by lazy {
        ActivityDetailMenuBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailProductViewModel by viewModels {
        GenericViewModelFactory.create(DetailProductViewModel(this, intent?.extras))
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
        binding.llLocation.setOnClickListener {
            viewModel.navigateToMaps()
        }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this){
            binding.btnAddToCart.text = it.toCurrencyFormat()
        }
        viewModel.productCountLiveData.observe(this){
            binding.tvCounter.text = it.toString()
        }
    }

    private fun bindProduct(product: Menu?) {
        product?.let { item ->
            binding.ivBannerDetail.load(item.imgUrl) {
                crossfade(true)
            }
            binding.tvMenuName.text = item.name
            binding.tvDetailPrice.text = item.price.toCurrencyFormat()
            binding.tvLocation.text = item.location
            binding.tvDetailDesc.apply {
                text = item.desc
                movementMethod = ScrollingMovementMethod()
            }
            binding.btnAddToCart.text = getString(R.string.text_add_to_cart, item.price.toInt())
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
