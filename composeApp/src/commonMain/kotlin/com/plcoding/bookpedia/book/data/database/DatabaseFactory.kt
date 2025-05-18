package com.plcoding.bookpedia.book.data.database

import androidx.room.RoomDatabase

// tạo ra file db theo từng platform và trả về builder

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<FavoriteBookDatabase>
}