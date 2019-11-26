package fi.samipsolutions.voitracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import fi.samipsolutions.voitracker.R
import fi.samipsolutions.voitracker.fragments.AboutFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            val fragment = AboutFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_info -> {
                val fragment = AboutFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                val intent1 = Intent(this, MapsActivity::class.java)
                startActivity(intent1)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}