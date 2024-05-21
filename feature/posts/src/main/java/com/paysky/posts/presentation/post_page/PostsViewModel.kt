package com.paysky.posts.presentation.post_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paysky.utils.extensions.catchError
import com.paysky.utils.states.DataState
import com.paysky.posts.data.request.Post
import com.paysky.posts.data.database.PostEntity
import com.paysky.posts.domain.usecase.PostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postUseCase: PostUseCase
) : ViewModel() {


    private val _insertDataState: MutableStateFlow<DataState<Post>> =
        MutableStateFlow(DataState.Loading)
    val insertDataState: StateFlow<DataState<Post>> get() = _insertDataState

    private val _updateDataState: MutableStateFlow<DataState<Post>> =
        MutableStateFlow(DataState.Loading)
    val updateDataState: StateFlow<DataState<Post>> get() = _updateDataState

    private val _deleteDataState: MutableStateFlow<DataState<Any>> =
        MutableStateFlow(DataState.Loading)
    val deleteDataState: StateFlow<DataState<Any>> get() = _deleteDataState

    private val _syncPostDataState: MutableStateFlow<DataState<List<Post>>> =
        MutableStateFlow(DataState.Loading)
    val syncPostDataState: StateFlow<DataState<List<Post>>> get() = _syncPostDataState


    fun insert(post: PostEntity) {
        viewModelScope.launch {
            postUseCase.insert(post)
                .catchError { _insertDataState.emit(DataState.Failure(it)) }
                .collect { _insertDataState.emit(DataState.Success(it)) }
        }
    }

    fun update(post: PostEntity) {
        viewModelScope.launch {
            postUseCase.update(post)
                .catchError { _updateDataState.emit(DataState.Failure(it)) }
                .collect { _updateDataState.emit(DataState.Success(it)) }
        }
    }

    fun delete(postId: Int) {
        viewModelScope.launch {
            postUseCase.delete(postId)
                .catchError { _deleteDataState.emit(DataState.Failure(it)) }
                .collect { _deleteDataState.emit(DataState.Success(it)) }
        }
    }

    fun syncData() {
        viewModelScope.launch {
            postUseCase.syncData()
                .catchError { _syncPostDataState.emit(DataState.Failure(it)) }
                .collect { _syncPostDataState.emit(DataState.Success(it)) }
        }
    }

}
