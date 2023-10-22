package com.bitohur.foodapp.data.dummy

import com.bitohur.foodapp.model.Categories

interface DummyCategoriesDataSource {
    fun getCategories(): List<Categories>
}

class DummyCategoriesDataSourceImpl(): DummyCategoriesDataSource {
    override fun getCategories(): List<Categories> = mutableListOf(
        Categories(
            name = "Nasi",
            imageUrl = "https://raw.githubusercontent.com/Bxtohur/FoodApp/master/app/src/main/res/drawable/ic_rice.png"
        ),
        Categories(
            name = "Mie",
            imageUrl = "https://raw.githubusercontent.com/Bxtohur/FoodApp/master/app/src/main/res/drawable/ic_mie.png"
        ),
        Categories(
            name = "Snack",
            imageUrl = "https://raw.githubusercontent.com/Bxtohur/FoodApp/master/app/src/main/res/drawable/ic_snack.png"
        ),
        Categories(
            name = "Minuman",
            imageUrl = "https://raw.githubusercontent.com/Bxtohur/FoodApp/master/app/src/main/res/drawable/ic_drink.png"
        )
    )

}