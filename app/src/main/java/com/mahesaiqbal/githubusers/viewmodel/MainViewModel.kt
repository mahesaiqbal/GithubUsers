package com.mahesaiqbal.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahesaiqbal.githubusers.core.model.GithubUsersResponseItem
import com.mahesaiqbal.githubusers.core.repository.MainRepository
import com.mahesaiqbal.githubusers.model.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> = _screenState

//    private val _users = MutableLiveData<List<UsersResponse>>()
//    val users: LiveData<List<UsersResponse>> = _users

    var users: List<GithubUsersResponseItem> = listOf()

    fun getUsers() {
        _screenState.postValue(ScreenState.LOADING)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getUsers()

                if (response.isSuccessful) {
                    // Check if user data is empty (soon)
                    _screenState.postValue(ScreenState.READY)

                    response.body()?.let { users = it }
                } else {
                    _screenState.postValue(ScreenState.EMPTY)
                    _screenState.postValue(ScreenState.ERROR("MainViewModel getUsers error code: ${response.code()}"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.postValue(ScreenState.EMPTY)
                _screenState.postValue(ScreenState.ERROR("MainViewModel getUsers exception error: ${e.message}"))
            }
        }
    }
}