package com.menard.ruralis.details

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity

data class Favorite (
    
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "photo")
    val photo: String? = null,

    @ColumnInfo(name = "fromRuralis")
    val fromRuralis: Boolean = false
)