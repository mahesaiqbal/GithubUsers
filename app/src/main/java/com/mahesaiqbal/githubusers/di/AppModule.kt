package com.mahesaiqbal.githubusers.di

import com.mahesaiqbal.githubusers.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MainViewModel(get()) }
}