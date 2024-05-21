package com.paysky.posts.domain.usecase

import com.paysky.posts.data.request.Post
import com.paysky.posts.data.database.PostEntity
import kotlinx.coroutines.flow.Flow

/**
 * @Created_by: Noor Elshafei
 * @Date: 4/20/2024
 */


interface PostUseCase {

    suspend fun insert(post: PostEntity): Flow<Post>
    suspend fun update(post: PostEntity): Flow<Post>
    suspend fun delete(postId: Int): Flow<Any>
    suspend fun syncData(): Flow<List<Post>>

}