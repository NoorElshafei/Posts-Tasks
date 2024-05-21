package com.paysky.posts.domain.usecase

import com.paysky.posts.data.request.Post
import com.paysky.posts.data.database.PostEntity
import com.paysky.posts.domain.repository.PostRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PostUseCaseImpl @Inject constructor(
    private val repository: PostRepository
) : PostUseCase {
    override suspend fun insert(post: PostEntity): Flow<Post> = repository.insert(post)

    override suspend fun update(post: PostEntity): Flow<Post> = repository.update(post)

    override suspend fun delete(postId: Int): Flow<Any> = repository.delete(postId)

    override suspend fun syncData(): Flow<List<Post>>  = repository.syncData()
}
