package com.mahesaiqbal.githubusers.core.model

import com.squareup.moshi.Json

data class UsersResponse(
    val login: String,
    val id: Int,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "repos_url")
    val reposUrl: String
)