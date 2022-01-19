package com.mahesaiqbal.githubusers.core.retrofit

import com.mahesaiqbal.githubusers.core.model.GithubUsersResponse
import com.mahesaiqbal.githubusers.core.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {

    @GET("users")
    suspend fun getUsers(): Response<GithubUsersResponse>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path(value = "username") username: String
    ): Response<UserResponse>
}