package com.mahesaiqbal.githubusers.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahesaiqbal.githubusers.adapter.MainAdapter
import com.mahesaiqbal.githubusers.core.model.GithubUsersResponseItem
import com.mahesaiqbal.githubusers.databinding.ActivityMainBinding
import com.mahesaiqbal.githubusers.model.ScreenState
import com.mahesaiqbal.githubusers.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
        viewModel.getUsers()

        binding.srlRefresh.setOnRefreshListener {
            viewModel.getUsers()
            binding.srlRefresh.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(this, Observer {
            when (it) {
                ScreenState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                ScreenState.READY -> {
                    binding.progressBar.visibility = View.GONE
                }
                ScreenState.EMPTY -> {
                    binding.progressBar.visibility = View.GONE
                }
                is ScreenState.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "MainActivity Error Get Data Users: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("MainActivity", "Error Get Data Users: ${it.message}")
                }
            }
        })

        viewModel.users.observe(this, Observer {
            if (it != null) {
                initData(it)
            }
        })

        viewModel.userDetail.observe(this, Observer {
            if (it != null) {
                Toast.makeText(
                    this,
                    "Username: ${it.login}\nEmail: ${it.email}\nCreated At: ${it.createdAt}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun initData(users: List<GithubUsersResponseItem>) {
        val mainAdapter = MainAdapter()
        mainAdapter.setData(users)
        mainAdapter.onItemClick = { selectedData ->
            viewModel.getUserDetail(selectedData.login)
        }

        with(binding.rvUsers) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mainAdapter
        }
    }
}