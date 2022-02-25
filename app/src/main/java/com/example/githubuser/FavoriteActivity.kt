package com.example.githubuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.db.UserHelper
import com.example.githubuser.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)

        val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.data != null) {
                when (result.resultCode) {
                    DetailUserActivity.RESULT_ADD -> {
                        val user = result?.data?.getParcelableExtra<User>(DetailUserActivity.EXTRA_USER) as User
                        adapter.addItem(user)
                        binding.rvFavorite.smoothScrollToPosition(adapter.itemCount - 1)
                    }
                    DetailUserActivity.RESULT_DELETE -> {
                        val position = result?.data?.getIntExtra(DetailUserActivity.EXTRA_POSITION, 0) as Int
                        adapter.removeItem(position)
                        Log.d("asd", "onCreate: $position")
                    }
                }
            }
        }

        adapter = UserAdapter(null, 3)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User?, position: Int?, username: String?) {
                val toDetailUser = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                toDetailUser.putExtra(DetailUserActivity.EXTRA_USER, data)
                toDetailUser.putExtra(DetailUserActivity.EXTRA_USERNAME, data?.username)
                toDetailUser.putExtra(DetailUserActivity.EXTRA_POSITION, position)
                resultLauncher.launch(toDetailUser)
            }

        })

        binding.rvFavorite.adapter = adapter

        loadUsersAsync()

    }

    private fun loadUsersAsync() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val users = deferredUsers.await()
            if (users.size > 0) {
                adapter.listUsers = users
            } else {
                adapter.listUsers = ArrayList()
            }
            userHelper.close()
        }
    }
}