package fi.samipsolutions.voitracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fi.samipsolutions.voitracker.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_about, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        aboutTextView1.text ="About the app"
    }
}