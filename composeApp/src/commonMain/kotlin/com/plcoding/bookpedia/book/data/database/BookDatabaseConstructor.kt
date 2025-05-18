@file:Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.plcoding.bookpedia.book.data.database

import androidx.room.RoomDatabaseConstructor

// đảm nhận việc tạo ra db bằng builder có sẵn
expect object BookDatabaseConstructor : RoomDatabaseConstructor<FavoriteBookDatabase> {
    override fun initialize(): FavoriteBookDatabase
}