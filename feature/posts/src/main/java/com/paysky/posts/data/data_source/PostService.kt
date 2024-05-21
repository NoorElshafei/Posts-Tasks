package com.paysky.posts.data.data_source

import com.paysky.posts.data.request.Post
import com.paysky.posts.data.utils.EndPoints
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostService {


    @GET(EndPoints.POSTS)
    fun getPosts(): List<Post>

    @POST(EndPoints.POSTS)
    fun createPost(@Body post: Post): Post

    @PUT(EndPoints.UPDATE)
    fun updatePost(@Path("id") id: Int, @Body post: Post): Post

    @DELETE(EndPoints.DELETE)
    fun deletePost(@Path("id") id: Int): Any
}
