package com.mahesaiqbal.githubusers.core.retrofit

import com.mahesaiqbal.githubusers.core.model.UsersResponse
import retrofit2.Response
import retrofit2.http.GET

interface Service {

    @GET("users")
    suspend fun getUsers(): Response<List<UsersResponse>>
}