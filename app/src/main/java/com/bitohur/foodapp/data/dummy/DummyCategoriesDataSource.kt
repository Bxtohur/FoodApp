package com.bitohur.foodapp.data.dummy

import com.bitohur.foodapp.model.Categories

interface DummyCategoriesDataSource {
    fun getCategories(): List<Categories>
}

class CategoriesDataSourceImpl(): DummyCategoriesDataSource {
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