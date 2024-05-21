package com.paysky.posts.domain.repository

import com.paysky.posts.data.request.Post
import com.paysky.posts.data.database.PostEntity
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    suspend fun insert(post: PostEntity): Flow<Post>
    suspend fun update(post: PostEntity): Flow<Post>
    suspend fun delete(postId: Int): Flow<Any>
    suspend fun syncData(): Flow<List<Post>>
}
