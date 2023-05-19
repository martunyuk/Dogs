package com.vitaly.dogs

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class HomeFragmentViewModel: ViewModel() {

    private val _requestData = MutableLiveData<RequestData>()
    fun requestData(): LiveData<RequestData> {
        return _requestData
    }

    init {
        _requestData.value = RequestData(JSONArray(), false)
    }

    fun loadRandomDogs() {
        val httpClient: OkHttpClient = OkHttpClient().newBuilder().build()
        val newRequest: Request = Request.Builder()
            .url("https://dog.ceo/api/breeds/image/random/50")
            .method("GET", null)
            .build()
        httpClient.newCall(newRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                _requestData.postValue(_requestData.value?.apply { isFailed = true })
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body!!.string())
                _requestData.postValue(_requestData.value?.apply { images = jsonObject.getJSONArray("message") })
            }
        })
    }

    class RequestData(var images: JSONArray, var isFailed: Boolean)
}