package com.example.networktest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.CacheResponse
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        send_request.setOnClickListener {
            sendRequestWithURLConnection()
        }
        send_request_OkHttp.setOnClickListener {
            sendRequestWithOkHttp()
        }
        second_activity.setOnClickListener {
            val intent = Intent(this,SecondActivity::class.java)
            startActivity(intent)
        }
        third_activity.setOnClickListener {
            val intent = Intent(this,ThirdActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sendRequestWithURLConnection() {
        thread {
            var conntection:HttpURLConnection? = null
            try {
                val response = StringBuilder()
                val url = URL("https://www.baidu.com")
                conntection = url.openConnection() as HttpURLConnection
                conntection.connectTimeout = 8000
                conntection.readTimeout = 8000
                val input = conntection.inputStream
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
                showResponse(response.toString())
            } catch (e:Exception) {
                e.printStackTrace()
            } finally {
                conntection?.disconnect()
            }
        }
    }
    private fun sendRequestWithOkHttp() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://www.douyin.com/")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) showResponse(responseData)
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun showResponse(response: String) {
        runOnUiThread {
//            ??????ui
            responseText.text = response
        }
    }
}