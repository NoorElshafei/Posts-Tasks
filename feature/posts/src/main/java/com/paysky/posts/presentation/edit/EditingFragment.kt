package com.paysky.posts.presentation.edit

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.paysky.feature.posts.R
import com.paysky.feature.posts.databinding.AddingFragmentBinding
import com.paysky.feature.posts.databinding.EditingFragmentBinding

import com.paysky.network.data.errors.getMessage
import com.paysky.network.data.errors.getType
import com.paysky.posts.data.database.PostEntity
import com.paysky.posts.data.request.Post
import com.paysky.posts.domain.enums.PostValidation
import com.paysky.posts.presentation.post_page.PostsViewModel
import com.paysky.ui.extensions.hide
import com.paysky.ui.extensions.show
import com.paysky.ui.presentation.BaseFragment
import com.paysky.utils.extensions.collect
import com.paysky.utils.extensions.observe
import com.paysky.utils.states.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditingFragment : BaseFragment<EditingFragmentBinding>(EditingFragmentBinding::inflate) {

    private val viewModel:PostsViewModel by navGraphViewModels(R.id.post_nav) {
        defaultViewModelProviderFactory
    }


    override fun bindViews() {
        initUI()
        subscribeOnObservers()
    }

    private fun initUI() {
        binding.editTextTitle.setText(viewModel.post.title)
        binding.editTextBody.setText(viewModel.post.body)
        binding.buttonSave.setOnClickListener {
            viewModel.update(
                PostEntity(
                    viewModel.post.id,
                    title = binding.editTextTitle.text.toString(),
                    userId = viewModel.post.userId,
                    body = binding.editTextBody.text.toString()
                )
            )
        }
    }


    private fun subscribeOnObservers() {
        observe(viewModel.validationAddEdit) { showValidation(it) }

        collect(viewModel.updateDataState) {
            when (it) {
                is DataState.Success -> {
                    hideLoading()
                    showMessage("updated successfully")
                    findNavController().popBackStack()
                }

                is DataState.Failure -> {
                    hideLoading()
                    showMessage(
                        it.throwable.getType().getMessage().text ?: "couldn't fetch data, try again"
                    )
                }

                DataState.Loading -> {
                    showLoading()
                }

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

    private fun showValidation(invalidField: Int?) {
        invalidField?.let {
            when (it) {
                PostValidation.EMPTY_TITLE.value -> {
                    showMessage("You must write a title")
                }
                PostValidation.EMPTY_BODY.value -> {
                    showMessage("You must write a Body")
                }
            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_posts

}
