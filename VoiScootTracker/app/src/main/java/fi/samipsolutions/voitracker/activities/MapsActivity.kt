package fi.samipsolutions.voitracker.activities

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import fi.samipsolutions.voitracker.R
import fi.samipsolutions.voitracker.adapters.CustomInfoWindowAdapter
import fi.samipsolutions.voitracker.adapters.ScootMarker
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener {

    private val TAG = MapsActivity::class.java.name
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var currentLatLng: LatLng
    private lateinit var current_location: Marker
    private val markers = mutableMapOf<Int, Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        MobileAds.initialize(this) {}

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation
                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        createLocationRequest()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        // return early if the map was not initialised properly
        mMap = googleMap ?: return
        val clusterManager = ClusterManager<ScootMarker>(this, mMap)

        with(mMap.uiSettings) {
            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = true
            isCompassEnabled = true
            isRotateGesturesEnabled = true
            isZoomGesturesEnabled = true
        }

        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)
        mMap.setOnInfoWindowClickListener(clusterManager)
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
        getData()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@MapsActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun getData() {
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this)
        val builder = StringBuilder()
        val url = builder
            .append("https://")
            .append("api.voiapp.io/v1/")
            .append("vehicle/status/ready?")
            .append("lat=60.22" + "&lng=22.22").toString()
        Log.d("JSON_URL", url)
        // Create request and listeners
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                for(i in 0 until response.length()) {
                    val item: JSONObject = response.getJSONObject(i)
                    val item_location = item.getJSONArray("location")
                    val color = giveMarkerColor(item)
                    val point = LatLng(item_location.getDouble(0), item_location.getDouble(1))
                    markers[i] = mMap.addMarker(
                        MarkerOptions()
                            .position(point)
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                            .title(item["short"]
                                .toString())
                            .snippet(giveSnippet(item))
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
                }
            },
            Response.ErrorListener { error ->
                Log.d("JSON_ERROR",error.toString())
            }
        )
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }


    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions()
            .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)))
            .position(location)
        current_location = mMap.addMarker(markerOptions)
    }

    private fun giveSnippet(item: JSONObject): String? {
        return ("Battery: " + item["battery"].toString() + "\n" + "Scoot QR code: " + item.getString("short"))
    }

    private fun giveMarkerColor(item: JSONObject): Float {
        return when(item.getString("serial")) {
            "null" -> BitmapDescriptorFactory.HUE_BLUE
            else -> BitmapDescriptorFactory.HUE_RED
        }
    }

    override fun onMarkerClick(p0: Marker?) = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }
}
