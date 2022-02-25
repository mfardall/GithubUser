package com.example.githubuser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment() {
    private lateinit var rvFollowers: RecyclerView
    private lateinit var username: String
    private lateinit var mActivity: DetailUserActivity
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvFollowers = view.findViewById(R.id.rv_followers)!!
        rvFollowers.setHasFixedSize(true)
        progressBar = view.findViewById(R.id.progressBar)

        mActivity = activity as DetailUserActivity
        username = mActivity.username

        findFollowers()
    }

    private fun findFollowers() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody)
                    }
                } else {
                    Log.e("asd", "Failure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e("asd", "Failure: ${t.message}")
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun setUserData(userData: List<UserResponse>) {
        val listUser = ArrayList<UserResponse>()
        for (data in userData) {
            listUser.add(data)
        }
        rvFollowers.layoutManager = LinearLayoutManager(view?.context)
        val userAdapter = UserAdapter(listUser, 1)
        rvFollowers.adapter = userAdapter

    }
}