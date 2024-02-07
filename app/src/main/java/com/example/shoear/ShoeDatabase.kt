package com.example.shoear

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//Creating the actual database object

@Database(entities = [Shoe::class, Users::class], version = 1, exportSchema = false)
abstract class ShoeDatabase : RoomDatabase() {
    abstract fun shoeDao(): ShoeDao

    //Define DAO's as function first

    companion object {

        //Volatile exists to ensure only one instantiation of the database exists at a time, improves performance
        @Volatile
        private var instance: ShoeDatabase? = null

        fun getInstance(context: Context?): ShoeDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context!!, ShoeDatabase::class.java, "shoes.db")
                    .allowMainThreadQueries() /* for convenience and brevity */
                    .createFromAsset("shoes.db")
                    .build()
            }
            return instance
        }
    }
}