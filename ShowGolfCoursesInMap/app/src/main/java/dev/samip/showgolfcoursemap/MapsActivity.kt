package dev.samip.showgolfcoursemap

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dev.samip.showgolfcoursemap.R
import org.json.JSONObject
import java.lang.Boolean.TRUE

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val queue = Volley.newRequestQueue(this)
        val url = "https://ptm.fi/materials/golfcourses/golf_courses.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val courses = response.getJSONArray("courses")

                for (i in 0 until courses.length()) {
                    val course: JSONObject = courses.getJSONObject(i)
                    val color = giveMarkerColor(course)
                    val point = LatLng(
                        course["lat"].toString().toDouble(),
                        course["lng"].toString().toDouble()
                    )
                    mMap.addMarker(
                        MarkerOptions()
                            .position(point)//.title(place["street"].toString()))
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                            .title(course["course"].toString())
                            .snippet(giveSnippet(course))
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
                    mMap.uiSettings.isZoomControlsEnabled = TRUE

                    mMap.setInfoWindowAdapter(object:GoogleMap.InfoWindowAdapter{
                        override fun getInfoWindow(arg0: Marker):View? {
                            return null
                        }
                        override fun getInfoContents(marker: Marker): View {
                            val context = applicationContext
                            val info = LinearLayout(context)
                            info.orientation = (LinearLayout.VERTICAL)
                            val title = TextView(context)
                            title.setTextColor(Color.BLACK)
                            title.gravity = Gravity.CENTER
                            title.setTypeface(null, Typeface.BOLD)
                            title.text = marker.title
                            val snippet = TextView(context)
                            snippet.setTextColor(Color.GRAY)
                            snippet.text = marker.snippet
                            info.addView(title)
                            info.addView(snippet)
                            return info
                        }
                    })


                }
            },
            Response.ErrorListener { e ->
                Log.d("JSON", e.toString())
            })
        queue.add(jsonObjectRequest)
    }

    private fun giveSnippet(course: JSONObject): String? {
        return (course["address"].toString() + "\n" + course["phone"] + "\n" + course["email"] + "\n" + course["web"])
    }

    private fun giveMarkerColor(course: JSONObject): Float {
        return when(course["type"]){
            "Kulta" -> BitmapDescriptorFactory.HUE_YELLOW
            "Kulta/Etu" -> BitmapDescriptorFactory.HUE_BLUE
            "Etu" -> BitmapDescriptorFactory.HUE_GREEN
            else -> BitmapDescriptorFactory.HUE_VIOLET
        }
    }
}