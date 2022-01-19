package com.mahesaiqbal.githubusers.model

sealed class ScreenState {
    object LOADING: ScreenState()
    object READY: ScreenState()
    object EMPTY: ScreenState()
    data class ERROR(val message: String): ScreenState()
}