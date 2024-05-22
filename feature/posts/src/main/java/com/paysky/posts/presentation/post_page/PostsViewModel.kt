package com.paysky.posts.presentation.post_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paysky.utils.extensions.catchError
import com.paysky.utils.states.DataState
import com.paysky.posts.data.request.Post
import com.paysky.posts.data.database.PostEntity
import com.paysky.posts.domain.enums.PostValidation
import com.paysky.posts.domain.usecase.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postUseCase: PostUseCase
) : ViewModel() {


    private val _insertDataState: MutableStateFlow<DataState<Post>> =
        MutableStateFlow(DataState.None)
    val insertDataState: StateFlow<DataState<Post>> get() = _insertDataState

    private val _updateDataState: MutableStateFlow<DataState<Post>> =
        MutableStateFlow(DataState.None)
    val updateDataState: StateFlow<DataState<Post>> get() = _updateDataState

    private val _deleteDataState: MutableStateFlow<DataState<Any>> =
        MutableStateFlow(DataState.Loading)
    val deleteDataState: StateFlow<DataState<Any>> get() = _deleteDataState

    private val _syncPostDataState: MutableStateFlow<DataState<List<Post>>> =
        MutableStateFlow(DataState.Loading)
    val syncPostDataState: StateFlow<DataState<List<Post>>> get() = _syncPostDataState

    private lateinit var _post: Post
    val post get() = _post
    private val validationAddEditLiveData = MutableLiveData<Int>()
    val validationAddEdit: LiveData<Int> get() = validationAddEditLiveData

    fun insert(post: PostEntity) {
        viewModelScope.launch {
            if (validateAddEdit(post)
            ) {
                postUseCase.insert(post)
                    .onStart { _insertDataState.emit(DataState.Loading) }
                    .catchError { _insertDataState.emit(DataState.Failure(it)) }
                    .collect {
                        _insertDataState.emit(DataState.Success(it))
                        _insertDataState.emit(DataState.None)
                    }
            }
        }
    }

    fun update(post: PostEntity) {
        if (validateAddEdit(post)
        ) {
            viewModelScope.launch {
                postUseCase.update(post)
                    .onStart { _updateDataState.emit(DataState.Loading) }
                    .catchError { _updateDataState.emit(DataState.Failure(it)) }
                    .collect {
                        _updateDataState.emit(DataState.Success(it))
                        _updateDataState.emit(DataState.None)
                    }
            }
        }
    }

    fun delete(postId: Int) {
        viewModelScope.launch {
            postUseCase.delete(postId)
                .onStart { _deleteDataState.emit(DataState.Loading) }
                .catchError { _deleteDataState.emit(DataState.Failure(it)) }
                .collect { _deleteDataState.emit(DataState.Success(it)) }
        }
    }

    fun syncData() {
        viewModelScope.launch {
            postUseCase.syncData()
                .onStart { _syncPostDataState.emit(DataState.Loading) }
                .catchError { _syncPostDataState.emit(DataState.Failure(it)) }
                .collect { _syncPostDataState.emit(DataState.Success(it)) }
        }
    }

    fun savePost(post: Post) {
        _post = Post(post.userId, post.id, post.title, post.body)
    }

    private fun validateAddEdit(
        post: PostEntity?
    ): Boolean {
        return if (post?.title.isNullOrEmpty()) {
            validationAddEditLiveData.value = PostValidation.EMPTY_TITLE.value
            false
        } else if (post?.body.isNullOrEmpty()) {
            validationAddEditLiveData.value = PostValidation.EMPTY_BODY.value
            false
        } else {
            true
        }

    }

}
