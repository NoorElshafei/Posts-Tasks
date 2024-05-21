package com.paysky.posts.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDao {

    @Query("SELECT * FROM posts WHERE isDeleted = 0")
    fun getPosts(): LiveData<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Update
    suspend fun update(post: PostEntity)

    @Query("UPDATE posts SET isDeleted = 1 WHERE id = :postId")
    suspend fun markAsDeleted(postId: Int)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun delete(postId: Int)
}
