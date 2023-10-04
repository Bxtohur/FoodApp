package com.bitohur.foodapp.presentation.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bitohur.foodapp.model.Menu

class DetailProductViewModel(private val context: Context, private val extras: Bundle?) : ViewModel() {
    val product = extras?.getParcelable<Menu>(DetailProductActivity.EXTRA_PRODUCT)

    val priceLiveData = MutableLiveData<Double>().apply {
        postValue(product?.price ?: 0.0)
    }
    val productCountLiveData = MutableLiveData<Int>().apply {
        postValue(1)
    }

    fun add() {
        val count = (productCountLiveData.value ?: 0) + 1
        productCountLiveData.postValue(count)
        priceLiveData.postValue(product?.price?.times(count) ?: 0.0)
    }

    fun minus() {
        if ((productCountLiveData.value ?: 0) > 1) {
            val count = (productCountLiveData.value ?: 0) - 1
            productCountLiveData.postValue(count)
            priceLiveData.postValue(product?.price?.times(count) ?: 0.0)
        }
    }

    fun navigateToMaps() {
        val location = product?.location
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            Toast.makeText(context, "Google Maps tidak terinstal", Toast.LENGTH_SHORT).show()
        } else {
            context.startActivity(mapIntent)
        }
    }
}
