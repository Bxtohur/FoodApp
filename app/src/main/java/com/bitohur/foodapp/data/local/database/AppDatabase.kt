package com.bitohur.foodapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bitohur.foodapp.data.dummy.DummyMenuDataSourceImpl
import com.bitohur.foodapp.data.local.database.AppDatabase.Companion.getInstance
import com.bitohur.foodapp.data.local.database.dao.CartDao
import com.bitohur.foodapp.data.local.database.dao.MenuDao
import com.bitohur.foodapp.data.local.database.entity.CartEntity
import com.bitohur.foodapp.data.local.database.entity.MenuEntity
import com.bitohur.foodapp.data.local.database.mapper.toMenuEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Database(
    entities = [CartEntity::class, MenuEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun menuDao(): MenuDao

    companion object {
        private const val DB_NAME = "FoodApp.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(DatabaseSeederCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class DatabaseSeederCallback(private val context: Context) : RoomDatabase.Callback() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            getInstance(context).menuDao().insertMenu(prepopulateMenus())
            getInstance(context).cartDao().insertCarts(prepopulateCarts())
        }
    }

    private fun prepopulateMenus(): List<MenuEntity> {
        return DummyMenuDataSourceImpl().getMenus().toMenuEntity()
    }

    private fun prepopulateCarts(): List<CartEntity> {
        return mutableListOf(
            CartEntity(
                id = 1,
                productId = 1,
                itemNotes = "Yang masih anget pokonya",
                itemQuantity = 3
            ),
            CartEntity(
                id = 2,
                productId = 2,
                itemNotes = "Banyakin kecap",
                itemQuantity = 6
            ),
        )
    }
}