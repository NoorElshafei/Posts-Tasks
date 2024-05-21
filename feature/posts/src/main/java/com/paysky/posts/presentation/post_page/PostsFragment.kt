package com.paysky.posts.presentation.post_page

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.fragment.app.viewModels
import com.paysky.feature.posts.R
import com.paysky.feature.posts.databinding.FragmentPostsBinding

import com.paysky.network.data.errors.getMessage
import com.paysky.network.data.errors.getType
import com.paysky.ui.extensions.hide
import com.paysky.ui.extensions.show
import com.paysky.ui.presentation.BaseFragment
import com.paysky.utils.extensions.collect
import com.paysky.utils.states.DataState
import com.paysky.posts.presentation.post_page.adapter.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostsFragment : BaseFragment<FragmentPostsBinding>(FragmentPostsBinding::inflate) {

    private val viewModel by viewModels<PostsViewModel>()
    @Inject
    lateinit var adapter: PostsAdapter

    override fun bindViews() {
        initUI()
        subscribeOnObservers()
    }

    private fun initUI() {
        binding.recyclerView.adapter = adapter
        viewModel.syncData()
    }


    private fun subscribeOnObservers() {
        collect(viewModel.syncPostDataState) {
            when (it) {
                is DataState.Success -> {
                    hideLoading()
                    adapter.submitList(it.data)
                    showRv()
                }

                is DataState.Failure -> {
                    hideLoading()
                    showMessage(
                        it.throwable.getType().getMessage().text ?: "couldn't fetch data, try again"
                    )
                    hideRv()

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

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network: Network? = connectivityManager.activeNetwork
                if (network != null) {
                    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                    return networkCapabilities != null &&
                            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                }
            } else {
                // For devices below Android M
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }
        return false
    }

    override fun getLayoutResId(): Int = R.layout.fragment_posts
}
