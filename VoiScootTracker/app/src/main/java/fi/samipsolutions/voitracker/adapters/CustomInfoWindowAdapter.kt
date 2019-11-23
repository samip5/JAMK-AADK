package fi.samipsolutions.voitracker.adapters

import android.app.Application
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter : Application(), GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(arg0: Marker): View? {
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
}