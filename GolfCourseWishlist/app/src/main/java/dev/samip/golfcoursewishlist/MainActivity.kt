package dev.samip.golfcoursewishlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Use StaggeredGridLayoutManager as a layout manager for recyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        // Use GolfCourseWishlistAdapter as a adapter for recyclerView
        recyclerView.adapter = GolfCourseWishlistAdapter(Places.placeList())
    }
}
