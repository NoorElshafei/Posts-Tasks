package com.paysky.posts.data.request

import com.paysky.ui.models.BaseModel

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
):BaseModel()