package com.paysky.posts.domain.repository

import android.content.Context
import android.net.ConnectivityManager
import com.paysky.network.di.IoDispatcher
import com.paysky.posts.data.data_source.PostService
import com.paysky.posts.data.database.PostDao
import com.paysky.posts.data.request.Post
import com.paysky.posts.data.database.PostEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    private val postDao: PostDao,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PostRepository {

    override suspend fun insert(post: PostEntity): Flow<Post> = flow {
        postDao.insert(post)
        if (isOnline(context)) {
            val response = postService.createPost(post.toPost())
            postDao.insert(response.toPostEntity())
            emit(response)
        }else{
            emit(post.toPost())
        }
    }.flowOn(dispatcher)


    override suspend fun update(post: PostEntity): Flow<Post> = flow {
        postDao.update(post)
        if (isOnline(context)) {
            val response = postService.updatePost(post.id, post.toPost())
            postDao.update(response.toPostEntity())
            emit(response)
        }else{
            emit(post.toPost())
        }
    }.flowOn(dispatcher)

    override suspend fun delete(postId: Int): Flow<Any> = flow {
        postDao.markAsDeleted(postId)
        if (isOnline(context)) {
            val response = postService.deletePost(postId)
            postDao.delete(postId)
            emit(response)
        }else{
            emit(Any())

        }
    }.flowOn(dispatcher)

    override suspend fun syncData(): Flow<List<Post>> = flow {
        if (isOnline(context)) {
            val response = postService.getPosts()
            response.forEach { post ->
                postDao.insert(post.toPostEntity())
            }
            emit(response)
        }else{
            val posts = arrayListOf<Post>()
            postDao.getPosts().value?.forEach { post ->
                posts.add(post.toPost())
            }
            emit(posts)
        }
    }.flowOn(dispatcher)

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun PostEntity.toPost(): Post {
        return Post(userId, id, title, body)
    }

    private fun Post.toPostEntity(): PostEntity {
        return PostEntity(id, userId, title, body)
    }
}