package fi.samipsolutions.voitracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fi.samipsolutions.voitracker.R
import kotlinx.android.synthetic.main.fragment_common.*

class AboutFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_common, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvCommon.text ="About the app"
        commonLayout.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
    }
}