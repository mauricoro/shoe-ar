package com.example.shoear

import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.ColumnInfo

//Creates the schema for the rooms database
//Needs to remain exactly the same as sqlite schema

@Entity(tableName = "Users")
data class Users(

   @ColumnInfo(name = "username") val username: String,
   @ColumnInfo(name = "id") val id: Int,
   @ColumnInfo(name = "password") val password: String,
   @PrimaryKey
   @ColumnInfo(name = "email") val email: String,
   @ColumnInfo(name = "footsize") val footsize: Int

)
