package com.mahesaiqbal.githubusers.core.repository

import com.mahesaiqbal.githubusers.core.model.UsersResponse
import com.mahesaiqbal.githubusers.core.retrofit.Service
import retrofit2.Response

class MainRepository(private val apiService: Service) {

    suspend fun getUsers(): Response<List<UsersResponse>> = apiService.getUsers()
}