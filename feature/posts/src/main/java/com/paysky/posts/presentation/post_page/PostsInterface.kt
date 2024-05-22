package com.paysky.posts.presentation.post_page

import com.paysky.posts.data.database.PostEntity
import com.paysky.posts.data.request.Post
import kotlinx.coroutines.flow.Flow

/**
 * @Created_by: Noor Elshafei
 * @Date: 5/21/2024
 */


interface PostsInterface {
    fun editPost(post: Post)
    fun deletePost(postId: Int)
}