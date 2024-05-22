package com.paysky.posts.presentation.post_page

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paysky.feature.posts.R
import com.paysky.feature.posts.databinding.FragmentPostsBinding

import com.paysky.network.data.errors.getMessage
import com.paysky.network.data.errors.getType
import com.paysky.posts.data.database.PostEntity
import com.paysky.posts.data.request.Post
import com.paysky.ui.extensions.hide
import com.paysky.ui.extensions.show
import com.paysky.ui.presentation.BaseFragment
import com.paysky.utils.extensions.collect
import com.paysky.utils.states.DataState
import com.paysky.posts.presentation.post_page.adapter.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostsFragment : BaseFragment<FragmentPostsBinding>(FragmentPostsBinding::inflate),PostsInterface {

    private val viewModel:PostsViewModel by navGraphViewModels(R.id.post_nav) {
        defaultViewModelProviderFactory
    }

    private val postAdapter by lazy { PostsAdapter(this) }


    override fun bindViews() {
        initUI()
        subscribeOnObservers()
    }

    private fun initUI() {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapter
        }
        binding.recyclerView.adapter = postAdapter
        viewModel.syncData()
        binding.buttonSync.setOnClickListener {
            viewModel.syncData()
        }
        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_postsFragment_to_addingFragment)
        }
    }


    private fun subscribeOnObservers() {
        collect(viewModel.syncPostDataState) {
            when (it) {
                is DataState.Success -> {
                    hideLoading()
                    postAdapter.submitList(it.data)
                  //  showRv()
                }

                is DataState.Failure -> {
                    hideLoading()
                    showMessage(
                        it.throwable.getType().getMessage().text ?: "couldn't fetch data, try again"
                    )
                    //hideRv()

                }

                DataState.Loading -> showLoading()

                DataState.None -> {}
            }
        }

        collect(viewModel.deleteDataState) {
            when (it) {
                is DataState.Success -> {
                    hideLoading()
                    showMessage("delete successfully")
                    viewModel.syncData()
                    //  showRv()
                }

                is DataState.Failure -> {
                    hideLoading()
                    showMessage(
                        it.throwable.getType().getMessage().text ?: "couldn't fetch data, try again"
                    )
                    //hideRv()

                }

                DataState.Loading -> showLoading()

                DataState.None -> {}
            }
        }

    }


    private fun hideLoading() {
        binding.loadingLayout.loading.hide()
    }

    private fun showLoading() {
        binding.loadingLayout.loading.show()
    }

    private fun showRv(){
        binding.recyclerView.show()
    }

    private fun hideRv(){
        binding.recyclerView.hide()
    }


    override fun getLayoutResId(): Int = R.layout.fragment_posts
    override fun editPost(post: Post) {
        viewModel.savePost(post)
        findNavController().navigate(R.id.action_postsFragment_to_editingFragment)

    }

    override fun deletePost(postId: Int) {
        viewModel.delete(postId)
    }
}
