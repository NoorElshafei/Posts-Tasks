package com.paysky.posts.presentation.diffCallback

import androidx.recyclerview.widget.DiffUtil
import com.paysky.posts.data.request.Post

class PostsDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) =
        oldItem.itemId == newItem.itemId

    override fun areContentsTheSame(oldItem: Post, newItem: Post) =
        oldItem == newItem
}
