package dev.samip.jamk.employeesfragmentsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dev.samip.jamk.employeesfragmentsapp.R
import dev.samip.jamk.employeesfragmentsapp.activities.MainActivity
import dev.samip.jamk.employeesfragmentsapp.adapters.EmployeesAdapter
import kotlinx.android.synthetic.main.item_detail.view.*


class DetailFragment : Fragment() {

    // create view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get root view
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // show employee if there is a selection made in recycler view's adapter
        if (EmployeesAdapter.position != -1) {
            val employee = MainActivity.employees.getJSONObject(EmployeesAdapter.position)
            // show data in UI
            employee?.let {
                rootView.nameTextView.text =
                    it.getString("lastName") + " " + it.getString("firstName")
                rootView.titleTextView.text = it.getString("title")
                rootView.emailTextView.text = it.getString("email")
                rootView.phoneTextView.text = it.getString("phone")
                val requestOptions = RequestOptions()
                requestOptions.override(500, 500)
                Glide.with(this).load(it.getString("image")).apply(requestOptions)
                    .into(rootView.imageViewItemDetail)
            }
        }
        // return view
        return rootView
    }
}