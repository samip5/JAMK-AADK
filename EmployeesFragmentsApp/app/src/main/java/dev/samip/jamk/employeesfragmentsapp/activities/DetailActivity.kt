package dev.samip.jamk.employeesfragmentsapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.samip.jamk.employeesfragmentsapp.R
import dev.samip.jamk.employeesfragmentsapp.fragments.DetailFragment

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, DetailFragment())
                .commit()
        }
    }
}
