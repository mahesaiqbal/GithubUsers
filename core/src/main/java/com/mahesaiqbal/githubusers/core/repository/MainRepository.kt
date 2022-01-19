package com.mahesaiqbal.githubusers.core.repository

import com.mahesaiqbal.githubusers.core.model.GithubUsersResponse
import com.mahesaiqbal.githubusers.core.retrofit.Service
import retrofit2.Response

class MainRepository(private val apiService: Service) {

    suspend fun getUsers(): Response<GithubUsersResponse> = apiService.getUsers()
}