package com.bitohur.foodapp.data

import com.bitohur.foodapp.model.Categories

interface CategoriesDataSource {
    fun getCategories(): List<Categories>
}

class CategoriesDataSourceImpl(): CategoriesDataSource{
    override fun getCategories(): List<Categories> = mutableListOf(
        Categories(
            categories = "Nasi",
            imgUrl = "https://raw.githubusercontent.com/Bxtohur/FoodApp/master/app/src/main/res/drawable/ic_rice.png"
        ),
        Categories(
            categories = "Mie",
            imgUrl = "https://raw.githubusercontent.com/Bxtohur/FoodApp/master/app/src/main/res/drawable/ic_mie.png"
        ),
        Categories(
            categories = "Snack",
            imgUrl = "https://raw.githubusercontent.com/Bxtohur/FoodApp/master/app/src/main/res/drawable/ic_snack.png"
        ),
        Categories(
            categories = "Minuman",
            imgUrl = "https://raw.githubusercontent.com/Bxtohur/FoodApp/master/app/src/main/res/drawable/ic_drink.png"
        )
    )

}