package com.mahesaiqbal.githubusers.core.retrofit

import com.mahesaiqbal.githubusers.core.model.GithubUsersResponse
import com.mahesaiqbal.githubusers.core.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {

    @GET("users")
    suspend fun getUsers(
        @Query(value = "since") since: Int,
        @Query(value = "per_page") perPage: Int
    ): Response<GithubUsersResponse>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path(value = "username") username: String
    ): Response<UserResponse>
}