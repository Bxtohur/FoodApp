package com.bitohur.foodapp.presentation.detail

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import coil.load
import com.bitohur.foodapp.R
import com.bitohur.foodapp.data.MenuDataSource
import com.bitohur.foodapp.data.MenuDataSourceImpl
import com.bitohur.foodapp.databinding.FragmentHomeBinding
import com.bitohur.foodapp.databinding.FragmentMenuDetailBinding
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.presentation.detail.MenuDetailFragmentArgs
import com.bitohur.foodapp.presentation.home.adapter.AdapterLayoutMode
import com.bitohur.foodapp.presentation.home.adapter.MenuListAdapter

class MenuDetailFragment : Fragment() {

    private fun Double.formatCurrency(currencySymbol: String): String {
        val formattedAmount = String.format("% ,.0f", this).replace("," , ".")
        return "$currencySymbol $formattedAmount"
    }

     private var count: Int = 1

    private lateinit var binding: FragmentMenuDetailBinding

    private val menu: Menu? by lazy {
        MenuDetailFragmentArgs.fromBundle(arguments as Bundle).menu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMenuData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.llLocation.setOnClickListener {
            navigateToMaps()
        }
        binding.ibBtnBack.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.btnMinus.setOnClickListener{
            minButton()
        }
        binding.btnPlus.setOnClickListener{
            addButton()
        }
    }

    private fun minButton() {
        if (count > 1) {
            count--
        }
        binding.tvCounter.text = count.toString()
        val total = menu?.price?.let { it * count } ?: 0
        binding.btnAddToCart.text = getString(R.string.text_add_to_cart, total.toInt())
    }

    private fun addButton() {
        count++
        binding.tvCounter.text = count.toString()
        val total = menu?.price?.let { it * count } ?: 0
        binding.btnAddToCart.text = getString(R.string.text_add_to_cart, total.toInt())
    }

    private fun navigateToMaps() {
        binding.llLocation.setOnClickListener {
            val location = menu?.location

            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(requireContext().packageManager) == null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(requireContext(), "Google Maps tidak terinstal", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showMenuData() {
        menu?.let { m ->
            binding.apply {
                ivBannerDetail.load(m.imgUrl) {
                    crossfade(true)
                }
                tvMenuName.text = m.name
                tvDetailPrice.text = m.price.formatCurrency("Rp.")
                tvLocation.text = m.location
                tvDetailDesc.apply {
                    text = m.desc
                    movementMethod = ScrollingMovementMethod()
                }
                btnAddToCart.text = getString(R.string.text_add_to_cart, m.price.toInt())
            }
        }
    }
}



