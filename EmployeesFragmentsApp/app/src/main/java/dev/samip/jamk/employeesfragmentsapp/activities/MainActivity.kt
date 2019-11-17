package dev.samip.jamk.employeesfragmentsapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dev.samip.jamk.employeesfragmentsapp.R
import dev.samip.jamk.employeesfragmentsapp.adapters.EmployeesAdapter
import kotlinx.android.synthetic.main.activity_detail.item_detail_container
import kotlinx.android.synthetic.main.item_list.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    // true if device is in landscape mode
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // is device landscape mode
        if (item_detail_container != null) {
            // The detail container view will be present only in the landscape
            // If this view is present, then the activity should be in two-pane mode.
            twoPane = true
        }

        // Load employees if not loaded, if loaded setup recycler view
        if (employees.length() == 0) loadJsonData()
        else setupRecyclerView(employees)
    }

    // Load JSON from the net
    private fun loadJsonData() {
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this)
        // URL to JSON data - remember use your own data here or "http://ptm.fi/data/android_employees.json"
        val url = "https://ptm.fi/data/android_employees.json"
        // Create request and listeners
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // store loaded json to static employees
                employees = response.getJSONArray("employees")
                // setup recycler view
                setupRecyclerView(employees)
            },
            Response.ErrorListener { error ->
                Log.d("JSON",error.toString())
            }
        )
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    // Add layout manager and adapter to recycler view
    private fun setupRecyclerView(employees: JSONArray) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = EmployeesAdapter(this, twoPane)
    }

    // employees static object - can be used as MainActivity.employees
    companion object {
        var employees: JSONArray = JSONArray()
    }
}
