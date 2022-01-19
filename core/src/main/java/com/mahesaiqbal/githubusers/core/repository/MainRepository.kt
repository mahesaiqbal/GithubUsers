package com.mahesaiqbal.githubusers.core.repository

import com.mahesaiqbal.githubusers.core.model.GithubUsersResponse
import com.mahesaiqbal.githubusers.core.model.UserResponse
import com.mahesaiqbal.githubusers.core.retrofit.Service
import retrofit2.Response

class MainRepository(private val apiService: Service) {

    suspend fun getUsers(since: Int, perPage: Int): Response<GithubUsersResponse> =
        apiService.getUsers(since, perPage)

    suspend fun getUserDetail(username: String): Response<UserResponse> =
        apiService.getUserDetail(username)
}