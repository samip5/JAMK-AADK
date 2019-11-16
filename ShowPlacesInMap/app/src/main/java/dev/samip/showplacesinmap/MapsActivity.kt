package dev.samip.showplacesinmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.Response
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mfromJson: ResponseModel

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    private fun fetchDataFromJson() {
        val remoteEndpoint = "https://samip.dev/android/map_data.json"

        val request = Request.Builder().url(remoteEndpoint).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("JSON", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    val body = response.body()?.string()
                    Log.i("JSON", body.toString())

                    val gson = GsonBuilder().create()


                    mfromJson = gson.fromJson(body, ResponseModel::class.java)
                    if(mfromJson.data != null) {
                        Log.d("onResponse_JSON", "There is data!")
                    }
                    for(item in mfromJson.data!!) {
                        val city = item!!.marker_name
                        val latutude_long = LatLng(item!!.lat!!, item.lo!!)
                        mMap.addMarker(MarkerOptions().position(latutude_long).title(city))
                    }
                    mMap.uiSettings.isZoomControlsEnabled = true
                    val kupittaa = LatLng(60.449446798243365, 22.291838048566213)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kupittaa, 14.0F))

                }
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        fetchDataFromJson()
    }
}
