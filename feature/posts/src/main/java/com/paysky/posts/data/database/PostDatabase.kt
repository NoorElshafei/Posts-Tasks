package com.paysky.posts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class], version = 1, exportSchema = true)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
