package dev.samip.employeesapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.employee_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text

// Employees Adapter, used in RecyclerView in MainActivity
class EmployeesAdapter(private val employees: JSONArray) : RecyclerView.Adapter<EmployeesAdapter.ViewHolder>() {

    // Create UI View Holder from XML layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeesAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.employee_item, parent, false)
        return ViewHolder(view)
    }

    // return item count in employees
    override fun getItemCount(): Int = employees.length()

    // bind data to UI View Holder
    override fun onBindViewHolder(holder: EmployeesAdapter.ViewHolder, position: Int) {
        // employee to bind UI
        val employee: JSONObject = employees.getJSONObject(position)

        // Get the URL from JSON
        val avatarUrl = employee["image"].toString()

        // Avatar
        Picasso.get().load(avatarUrl).into(holder.avatarView)
        // name
        holder.nameTextView.text = employee["last_name"].toString() + " " + employee["first_name"].toString()
        // Title
        holder.titleTextView.text = employee["title"].toString()
        // Department
        holder.departmentTextView.text = employee["department"].toString()

    }


    // View Holder class to hold UI views
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.nameTextView
        val titleTextView: TextView = view.titleTextView
        val avatarView: ImageView = view.employeeAvatarView
        val departmentTextView: TextView = view.departmentTextView

        init {
            itemView.setOnClickListener {
                val intent = Intent(view.context, EmployeeActivity::class.java)

                intent.putExtra("employee",employees[adapterPosition].toString())

                view.context.startActivity(intent)
            }
        }
    }
}