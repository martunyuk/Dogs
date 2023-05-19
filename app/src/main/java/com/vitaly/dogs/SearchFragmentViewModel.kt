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

class SearchFragmentViewModel: ViewModel() {

    var lastSelectedBreedId = 0

    private val _requestBreedsData = MutableLiveData<RequestBreedsData>()
    fun requestBreedsData(): LiveData<RequestBreedsData>{
        return _requestBreedsData
    }

    private val _requestDogsByBreed = MutableLiveData<RequestDogsByBreed>()
    fun requestDogsByBreed(): LiveData<RequestDogsByBreed>{
        return _requestDogsByBreed
    }

    init {
        _requestBreedsData.value = RequestBreedsData(ArrayList(), false)
        _requestDogsByBreed.value = RequestDogsByBreed(JSONArray(), false)
    }

    fun loadAllBreeds(){
        val httpClient: OkHttpClient = OkHttpClient().newBuilder().build()
        val newRequest: Request = Request.Builder()
            .url("https://dog.ceo/api/breeds/list/all")
            .method("GET", null)
            .build()
        httpClient.newCall(newRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                _requestBreedsData.postValue(_requestBreedsData.value?.apply { isFailed = true })
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body!!.string())
                val allBreeds = jsonObject.getJSONObject("message")
                val iterator: Iterator<String> = allBreeds.keys()
                val breedsArrayList = ArrayList<String>()
                while (iterator.hasNext()) {
                    val key = iterator.next()
                    breedsArrayList.add(key)
                }
                _requestBreedsData.postValue(_requestBreedsData.value?.apply { breeds = breedsArrayList })
            }
        })
    }

    fun loadDogsByBreed(breed: String){
        val httpClient: OkHttpClient = OkHttpClient().newBuilder().build()
        val newRequest: Request = Request.Builder()
            .url("https://dog.ceo/api/breed/$breed/images")
            .method("GET", null)
            .build()
        httpClient.newCall(newRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                _requestDogsByBreed.postValue(_requestDogsByBreed.value?.apply { isFailed = true })
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body!!.string())
                _requestDogsByBreed.postValue(_requestDogsByBreed.value?.apply { images = jsonObject.getJSONArray("message") })
            }
        })
    }

    class RequestBreedsData(var breeds: ArrayList<String>, var isFailed: Boolean)
    class RequestDogsByBreed(var images: JSONArray, var isFailed: Boolean)

}