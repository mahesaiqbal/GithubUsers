package com.mahesaiqbal.githubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahesaiqbal.githubusers.core.model.GithubUsersResponseItem
import com.mahesaiqbal.githubusers.core.model.UserResponse
import com.mahesaiqbal.githubusers.core.repository.MainRepository
import com.mahesaiqbal.githubusers.model.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> = _screenState

    private val _users = MutableLiveData<List<GithubUsersResponseItem>>()
    val users: LiveData<List<GithubUsersResponseItem>> = _users

    private val _userDetail = MutableLiveData<UserResponse>()
    val userDetail: LiveData<UserResponse> = _userDetail

    fun getUsers() {
        _screenState.postValue(ScreenState.LOADING)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getUsers()

                if (response.isSuccessful) {
                    // Check if user data is empty (soon)
                    _screenState.postValue(ScreenState.READY)

                    _users.postValue(response.body())
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

    fun getUserDetail(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getUserDetail(username)

                if (response.isSuccessful) {
                    _userDetail.postValue(response.body())
                } else {
                    Log.d("MainViewModel", "getUserDetail error code: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("MainViewModel", "getUserDetail exception error: ${e.message}")
            }
        }
    }
}