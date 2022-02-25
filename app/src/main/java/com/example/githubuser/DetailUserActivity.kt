package com.example.githubuser

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.db.DatabaseContract
import com.example.githubuser.db.UserHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    lateinit var username: String
    private var user: User? = null
    private var position: Int = 0
    private lateinit var userHelper: UserHelper
    private var avatarUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USERNAME)!!
        user = intent.getParcelableExtra(EXTRA_USER)

        getUser()
        setTabPager()
        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()
        if (user != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
        } else {
            user = User()
        }
        if (userHelper.isExist(username)) {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this@DetailUserActivity, R.drawable.ic_baseline_favorite_24_red))
            binding.fabFavorite.setOnClickListener {
                binding.fabFavorite.isEnabled = false
                binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this@DetailUserActivity, R.drawable.ic_baseline_favorite_24))
                val result = userHelper.deleteByUsername(username).toLong()
                if (result > 0) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_POSITION, position)
                    setResult(RESULT_DELETE, intent)
                    Toast.makeText(this@DetailUserActivity, "Berhasil menghapus data", Toast.LENGTH_SHORT).show()
                    refresh()
                } else {
                    Toast.makeText(this@DetailUserActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.fabFavorite.setOnClickListener {
                binding.fabFavorite.isEnabled = false
                binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this@DetailUserActivity, R.drawable.ic_baseline_favorite_24_red))
                user?.name = binding.tvUserName.text.toString()
                user?.username = binding.tvUserUsername.text.toString()
                user?.avatar = avatarUrl
                user?.isFavorite = 1

                val intent = Intent()
                intent.putExtra(EXTRA_USER, user)
                intent.putExtra(EXTRA_POSITION, position)

                val values = ContentValues()
                val dbColumn = DatabaseContract.UserColumns

                values.put(dbColumn.NAME, binding.tvUserName.text.toString())
                values.put(dbColumn.USERNAME, binding.tvUserUsername.text.toString())
                values.put(dbColumn.AVATAR, avatarUrl)
                values.put(dbColumn.ISFAVORITE, 1)

                val result = userHelper.insert(values)
                if (result > 0) {
                    user?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    Toast.makeText(
                        this@DetailUserActivity,
                        "Berhasil menambah data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@DetailUserActivity,
                        "Gagal menambah data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                refresh()
            }
        }
    }

    private fun refresh() {
        finish()
        overridePendingTransition(0,0)
        startActivity(intent)
        overridePendingTransition(0,0)
    }

    private fun setTabPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    private fun getUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody)
                    }
                } else {
                    Log.e("DetailUserActivity", "Failure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("DetailUserActivity", "Failure: ${t.message}")
            }

        })
    }

    private fun setUserData(data: UserResponse?) {
        binding.tvUserName.text = data?.name
        binding.tvUserUsername.text = data?.login
        binding.tvUserCompany.text = if (data?.company != null) data.company.toString() else "-"
        binding.tvUserLocation.text = if (data?.location != null) data.location.toString() else "-"
        binding.tvUserRepository.text = data?.publicRepos.toString()
        binding.tvUserFollowing.text = data?.following.toString()
        binding.tvUserFollowers.text = data?.followers.toString()
        avatarUrl = data?.avatarUrl

        Glide .with(applicationContext)
            .load(data?.avatarUrl)
            .into(binding.ivUser)

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_POSITION = "extra_position"
        private val TAB_TITLES = arrayOf(
            "Followers",
            "Following"
        )
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 201

    }

}