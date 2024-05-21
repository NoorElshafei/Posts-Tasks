package com.paysky.posts.presentation.post_page.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.paysky.posts.data.request.Post
import com.paysky.posts.presentation.diffCallback.PostsDiffCallback
import javax.inject.Inject

class PostsAdapter @Inject constructor() :
    ListAdapter<Post, PostsHolder>(PostsDiffCallback()) {

        override fun onBindViewHolder(holder: PostsHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsHolder =
        PostsHolder.from(parent)
}
