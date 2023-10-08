package com.bitohur.foodapp.data.dummy

import com.bitohur.foodapp.model.Menu

interface DummyMenuDataSource {
    fun getMenus(): List<Menu>
}

class MenuDataSourceImpl(): DummyMenuDataSource {
    override fun getMenus(): List<Menu> = mutableListOf(
        Menu(
            name = "Ayam Bakar",
            price = 50000.0,
            desc = "Ayam bakar adalah hidangan ayam yang dimasak dengan cara dipanggang hingga memiliki rasa gurih dan aromatik yang lezat.",
            menuImgUrl = "https://images.unsplash.com/photo-1592011432621-f7f576f44484?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80",
            location = "Jl. Mohtar Obing Trip, Gunungparang, Kec. Cikole, Kota Sukabumi, Jawa Barat"),
        Menu(
            name = "Ayam Goreng",
            price = 40000.0,
            desc = "Ayam goreng adalah potongan ayam yang digoreng hingga kulitnya renyah dan dagingnya empuk.",
            menuImgUrl = "https://images.unsplash.com/photo-1562967914-608f82629710?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2073&q=80",
            location = "Jl. Jend. Sudirman, Awirarangan, Kec. Kuningan, Kabupaten Kuningan, Jawa Barat 45511"),
        Menu(
            name = "Ayam Geprek",
            price = 40000.0,
            desc = "Ayam geprek adalah hidangan ayam goreng yang kemudian dihancurkan dan disajikan dengan sambal pedas.",
            menuImgUrl = "https://images.unsplash.com/photo-1674483950016-8ece0632914e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80",
            location = "Jl. Raya Jatinagara, Jatinagara, Kec. Jatinagara, Kabupaten Ciamis, Jawa Barat"),
        Menu(
            name = "Sate Usus Ayam",
            price = 5000.0,
            desc = "Sate usus ayam adalah makanan kecil terbuat dari usus ayam yang ditusuk, dibumbui, dan dipanggang di atas bara api, sering disajikan dengan saus kacang atau bumbu khas sate.",
            menuImgUrl = "https://images.unsplash.com/photo-1634871572365-8bc444e6faea?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80",
            location = "Jl. Flamboyan, Kalikoa, Kec. Kedawung, Kabupaten Cirebon, Jawa Barat 45154")
    )
}