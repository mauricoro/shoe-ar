package com.example.shoear

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

//Creates the schema for the rooms database
//Needs to remain exactly the same as sqlite schema

@Entity(tableName = "Shoe_Models")
data class Shoe(

   @ColumnInfo(name = "Cart") val inCart: Boolean,
   @ColumnInfo(name = "Info") val story: String?,
   @ColumnInfo(name = "Price") val price: String?,
   @ColumnInfo(name = "Image") val picture: String?,
   @ColumnInfo(name = "Models") val models: String,
   @ColumnInfo(name = "ModelName") val modelname: String,
   @PrimaryKey
   @ColumnInfo(name = "id") val id: Int

)