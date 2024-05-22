package com.paysky.posts.presentation.post_page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paysky.feature.posts.databinding.ItemPostBinding
import com.paysky.posts.data.database.PostEntity
import com.paysky.posts.data.request.Post
import com.paysky.posts.presentation.post_page.PostsInterface


class PostsHolder(
    private val binding: ItemPostBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post, postsInterface: PostsInterface) {
        binding.textViewTitle.text=post.title
        binding.textViewBody.text=post.body

        binding.buttonEdit.setOnClickListener {
            postsInterface.editPost(post)
        }
        binding.buttonDelete.setOnClickListener {
            postsInterface.deletePost(post.id)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup
        ): PostsHolder {
            val binding = ItemPostBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return PostsHolder(binding)
        }
    }


}
