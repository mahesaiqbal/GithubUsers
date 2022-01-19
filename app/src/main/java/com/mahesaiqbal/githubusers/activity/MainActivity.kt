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

    private var isLoading = false

    private val linearLayoutManager = LinearLayoutManager(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
        viewModel.getUsers(since, perPage)

        binding.srlRefresh.setOnRefreshListener {
            // Return to the start page (10 data that appears)
            since = 0
            perPage = 10
            viewModel.getUsers(since, perPage)
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
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = mainAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    Log.d(TAG, "scrollListener dx: $dx")
                    Log.d(TAG, "scrollListener dy: $dy")

                    if (dy > 0) {
                        var childCount = linearLayoutManager.childCount
                        var findFirstCompletelyVisibleItemPosition =
                            linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                        var itemCount = mainAdapter.itemCount
                        var lastItemInPage = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                        // Retrieve user data "id" based on the last item of the page to be the value "since"
//                        var lastUserDataId = if (lastItemInPage == perPage) users[lastItemInPage].id

                        Log.d(TAG, "scrollListener childCount: $childCount")
                        Log.d(TAG, "scrollListener findFirstCompletelyVisibleItemPosition: $findFirstCompletelyVisibleItemPosition")
                        Log.d(TAG, "scrollListener itemCount: $itemCount")
                        Log.d(TAG, "scrollListener lastItemInPage: $lastItemInPage")
//                        Log.d(TAG, "scrollListener lastUserDataId: $lastUserDataId")

                        if (!isLoading) {
                            if (childCount + findFirstCompletelyVisibleItemPosition >= itemCount) {
                                Log.d(TAG, "ItemVisibleCount next paging: ${childCount + findFirstCompletelyVisibleItemPosition}")
//                                loadMore()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun loadMore(lastUserDataId: Int) {
        isLoading = true
        binding.progressBarPaging.visibility = View.VISIBLE

        since = lastUserDataId
        perPage += 10

        if (perPage == 100) {
            // Stop loading data
            isLoading = false
            binding.progressBarPaging.visibility = View.GONE
        } else {
            // Load data according to "since" and "per_page" data
            viewModel.getUsers(since, perPage)
            isLoading = false
            binding.progressBarPaging.visibility = View.GONE
        }
    }
}