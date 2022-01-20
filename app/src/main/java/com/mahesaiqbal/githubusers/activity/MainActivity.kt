package com.mahesaiqbal.githubusers.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahesaiqbal.githubusers.adapter.MainAdapter
import com.mahesaiqbal.githubusers.core.model.GithubUsersResponseItem
import com.mahesaiqbal.githubusers.databinding.ActivityMainBinding
import com.mahesaiqbal.githubusers.model.ScreenState
import com.mahesaiqbal.githubusers.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val viewModel: MainViewModel by inject()

    private lateinit var binding: ActivityMainBinding

    // A user ID. Only return users with an ID greater than this ID.
    private var since = 0
    // Results per page (max 100)
    private var perPage = 10
    // Loading for pagination
    private var isPaginateLoading = false
    // Loading for data users for the first time
    private var isFirstTimeLoading = true
    // Container list every time calling users per_page data
    private var usersDisplay: MutableList<GithubUsersResponseItem> = mutableListOf()

    private var mainAdapter: MainAdapter = MainAdapter()

    private val linearLayoutManager = LinearLayoutManager(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()

        isFirstTimeLoading = true

        if (isFirstTimeLoading) {
            viewModel.getUsers(since, perPage)
        }

        binding.srlRefresh.setOnRefreshListener {
            // Return to the start page (10 data that appears)
            isFirstTimeLoading = true

            if (isFirstTimeLoading) {
                since = 0
                perPage = 10
                viewModel.getUsers(since, perPage)
            }

            binding.srlRefresh.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(this, Observer {
            when (it) {
                ScreenState.LOADING -> {
                    showLoading()
                }
                ScreenState.READY -> {
                    hideLoading()
                    hideEmptyLayout()
                    binding.rvUsers.visibility = View.VISIBLE
                }
                ScreenState.EMPTY -> {
                    hideLoading()
                    showEmptyLayout()
                }
                is ScreenState.ERROR -> {
                    hideLoading()
                    showEmptyLayout()
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
                if (it.size == 10) {
                    usersDisplay += it
                }

                initData()
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

    private fun initData() {
        mainAdapter.setData(usersDisplay)
        mainAdapter.onItemClick = { selectedData ->
            viewModel.getUserDetail(selectedData.login)
        }

        with(binding.rvUsers) {
            visibility = View.VISIBLE
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = mainAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        var childCount = linearLayoutManager.childCount
                        var findFirstCompletelyVisibleItemPosition =
                            linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                        var itemCount = mainAdapter.itemCount
                        var lastItemInPage = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                        if (!isPaginateLoading) {
                            if (childCount + findFirstCompletelyVisibleItemPosition >= itemCount) {
                                since = usersDisplay[lastItemInPage].id
                                loadMore()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun loadMore() {
        isFirstTimeLoading = false
        isPaginateLoading = true
        binding.progressBarPaging.visibility = View.VISIBLE

        if (isPaginateLoading) {
            // Load data according to "since" and "per_page" data
            viewModel.getUsers(since, perPage)
            isPaginateLoading = false
            binding.progressBarPaging.visibility = View.GONE
        }
    }

    private fun showLoading() {
        if (isFirstTimeLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBarPaging.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.progressBarPaging.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.progressBarPaging.visibility = View.GONE
    }

    private fun showEmptyLayout() {
        binding.rvUsers.visibility = View.GONE
        binding.rlViewEmpty.visibility = View.VISIBLE
    }

    private fun hideEmptyLayout() {
        binding.rlViewEmpty.visibility = View.GONE
    }
}