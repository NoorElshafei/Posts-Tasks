package com.paysky.posts.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isSynced: Boolean = true, // To track synchronization status
    val isDeleted: Boolean = false // To track deletion status
)