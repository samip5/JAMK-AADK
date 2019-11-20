package fi.samipsolutions.voitracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fi.samipsolutions.voitracker.R
import fi.samipsolutions.voitracker.data.VoiData
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    // true if device is in landscape mode
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadVehicles()
    }

    private fun loadVehicles() {
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this)
        val builder = StringBuilder()
        val url = builder
            .append("https://")
            .append("api.voiapp.io/v1/")
            .append("vehicle/status/ready?")
            .append("lat=60.451813&lng=22.266630").toString()
        Log.i("JSON_URL", url)
        // Create request and listeners
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val json_data = response.getJSONArray(0)
                for(i in 0 until json_data.length()) {
                    val item = json_data.getJSONArray(i)
                    if (item["type"] != "como") {
                        Log.d("JSON_SUCCESS", item["type"].toString())
                    }
                }
            },
            Response.ErrorListener { error ->
                Log.d("JSON_ERROR",error.toString())
            }
        )
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }
}
