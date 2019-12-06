package com.example.bitcointracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bitcoin.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class Bitcoin : AppCompatActivity() {
    // We will use the Coin Desk API to get Bitcoin price
    val URL = "https://api.coindesk.com/v1/bpi/currentprice.json"
    var okHttpClient: OkHttpClient = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitcoin)
        loadBitcoinprice()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh_bitcoin -> {
                loadBitcoinprice()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun loadBitcoinprice() {
        progressBar.visibility = View.VISIBLE
        val request: Request = Request.Builder().url(URL).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string()
                // we get the json response returned by the Coin Desk API
                // make this call on a browser for example to watch the properties
                // here we get USD and EUR rates properties
                // we split the value got just to keep the integer part of the values
                val usdRate =
                    (JSONObject(json.toString()).getJSONObject("bpi").getJSONObject("USD")["rate"] as String).split(
                        "."
                    )[0]
                val eurRate =
                    (JSONObject(json.toString()).getJSONObject("bpi").getJSONObject("EUR")["rate"] as String).split(
                        "."
                    )[0]
                val gbprate =
                    (JSONObject(json.toString()).getJSONObject("bpi").getJSONObject("GBP")["rate"] as String).split(
                        "."
                    )[0]

                runOnUiThread {
                    progressBar.visibility = View.GONE
                    bitcoinValues.text = usdRate
                    bitcoinValues2.text = gbprate
                    bitcoinValues3.text = eurRate
                }
            }
        })

    }
}
