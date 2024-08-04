package com.halil.e_marketcase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.halil.e_marketcase.data.CartItem
import com.halil.e_marketcase.data.Product

@Database(entities = [Product::class, CartItem::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
}
