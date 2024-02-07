package com.example.shoear

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query


//DAO's are Room's version of queries
//This isn't any simpler for us because we aren't using default queries, but if we were this would be really nice

@Dao
interface ShoeDao {

    @Query("SELECT Models FROM Shoe_Models WHERE ModelName LIKE :name")
    fun getmodel(name: String): String

    @Query("SELECT * FROM Shoe_Models ORDER BY id DESC")
    fun getShoes(): MutableList<Shoe>

    @Query("SELECT * FROM Shoe_Models WHERE id = :id")
    fun getShoeById(id: Int): Shoe

    @Query("SELECT * FROM Shoe_Models WHERE Cart = 1")
    fun getCart(): MutableList<Shoe>

    @Query("UPDATE Shoe_Models SET Cart = 1 WHERE id = :id")
    fun addCart(id: Int)

    @Query("UPDATE Shoe_Models SET Cart = 0 WHERE id = :id")
    fun rmCart(id: Int)

    @Query("SELECT Image FROM Shoe_Models WHERE ModelName LIKE :name")
    fun getpicture(name: String): String

    @Query("SELECT * FROM Users WHERE email LIKE :email AND password LIKE:password")
    fun getUser(email: String, password: String): Users?

    @Query("SELECT * FROM Users WHERE email LIKE :email")
    fun getUserByEmail(email: String): Users

    @Query("SELECT * FROM Users WHERE id = :id")
    fun getUserById(id: Int): Users

    @Query("UPDATE Users SET footsize = :footsize WHERE id = :id")
    fun updateFootsize(footsize: Int, id: Int)

    @Insert(onConflict = REPLACE)
    fun insertUser(user: Users)

    @Delete
    fun deleteUser(user: Users)
}
