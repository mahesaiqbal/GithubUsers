package com.mahesaiqbal.githubusers.core.retrofit

import com.mahesaiqbal.githubusers.core.model.GithubUsersResponse
import retrofit2.Response
import retrofit2.http.GET

interface Service {

    @GET("users")
    suspend fun getUsers(): Response<GithubUsersResponse>
}