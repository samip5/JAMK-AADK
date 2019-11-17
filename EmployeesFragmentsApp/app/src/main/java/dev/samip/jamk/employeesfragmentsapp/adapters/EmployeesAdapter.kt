package dev.samip.jamk.employeesfragmentsapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dev.samip.jamk.employeesfragmentsapp.R
import dev.samip.jamk.employeesfragmentsapp.activities.DetailActivity
import dev.samip.jamk.employeesfragmentsapp.activities.MainActivity
import dev.samip.jamk.employeesfragmentsapp.fragments.DetailFragment
import kotlinx.android.synthetic.main.employee_item.view.*
import org.json.JSONObject


// Employees Adapter
class EmployeesAdapter(
    private val parentActivity: MainActivity,
    private val twoPane: Boolean
) : RecyclerView.Adapter<EmployeesAdapter.ViewHolder>() {
    private val onClickListener: View.OnClickListener

    companion object {
        var position: Int = 0
    }
    init {
        // initialize click listener code
        onClickListener = View.OnClickListener { v ->
            // get the item position - look also onBindViewHolder code below
            position = v.tag as Int
            // if landscape -> show detail fragment
            if (twoPane) {
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, DetailFragment())
                    .commit()
                // if portrait -> use explicit intent to show employee
            } else {
                val intent = Intent(v.context, DetailActivity::class.java)
                v.context.startActivity(intent)
            }
        }
        // show list and detail side by side (on startup)
        if (twoPane) {
            parentActivity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, DetailFragment())
                .commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.employee_item, parent, false)
        return ViewHolder(view)
    }

    // return item count in employees
    override fun getItemCount(): Int = MainActivity.employees.length()

    // bind data to UI View Holder
    override fun onBindViewHolder(holder: EmployeesAdapter.ViewHolder, position: Int) {
        // employee to bind UI
        val employee: JSONObject = MainActivity.employees.getJSONObject(position)
        // name
        holder.nameTextView.text = employee["lastName"].toString() + " " + employee["firstName"].toString()
        // title
        holder.titleTextView.text = employee["title"].toString()
        // email
        holder.emailTextView.text = employee["email"].toString()
        // phone
        holder.phoneTextView.text = employee["phone"].toString()
        // department
        holder.departmentTextView.text = employee["department"].toString()
        // image
        Glide.with(holder.imageView.context).load(employee["image"].toString()).into(holder.imageView)
        // add position tag and click listener to holder view
        with(holder.itemView) {
            tag = position
            setOnClickListener(onClickListener)
        }
    }

    // View Holder class to hold UI views
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.nameTextView
        val titleTextView: TextView = view.titleTextView
        val emailTextView: TextView = view.emailTextView
        val phoneTextView: TextView = view.phoneTextView
        val departmentTextView: TextView = view.departmentTextView
        val imageView: ImageView = view.imageView
    }
}

