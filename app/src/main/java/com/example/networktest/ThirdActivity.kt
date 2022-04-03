package com.example.networktest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_third.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.concurrent.thread

class ThirdActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        JSON_Test.setOnClickListener {
            sendRequestWithJSON()
        }
        get_app_data.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.43.233/")  
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val appService = retrofit.create(AppService::class.java)
            appService.getAddData().enqueue(object:Callback<List<App>> {
                override fun onResponse(call: Call<List<App>>, response: Response<List<App>>) {
                    val list = response.body()
                    if (list != null) {
                        for (app in list) {
                            Log.e("Third Activity","id is ${app.id}")
                            Log.e("Third Activity","name is ${app.name}")
                            Log.e("Third Activity","version is ${app.version}")
                        }
                    }
                }
                override fun onFailure(call: Call<List<App>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    private fun sendRequestWithJSON() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://192.168.43.233/get_data.json")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) parseJSONWithJsonObj(responseData)
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseJSONWithJsonObj(jsonDate: String) {
        try {
            val jsonArray = JSONArray(jsonDate)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getString("id")
                val name = jsonObject.getString("name")
                val version = jsonObject.getString("version")

                Log.e("ThirdActivity",id)
                Log.e("ThirdActivity",name)
                Log.e("ThirdActivity",version)
            }
            json_data.text = jsonArray.getJSONObject(0).getString("name")
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
}