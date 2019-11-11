package dev.samip.employeesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_employee.*
import org.json.JSONObject

class EmployeeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        // get data
        val bundle: Bundle? = intent.extras;
        if (bundle != null) {
            val employeeString = bundle.getString("employee")
            val employee = JSONObject(employeeString)
            val fullName = employee["last_name"].toString() + " " + employee["first_name"].toString()
            val title = employee["title"].toString()
            val depart = employee["department"].toString()
            val email = employee["email"].toString()

            employeeName.text = fullName
            employeeTitle.text = title
            employeeDepart.text = depart
            employeeEmail.text = email

            // Get the URL from JSON
            val avatarUrl = employee["image"].toString()
            Picasso.get().load(avatarUrl).resize(300,300).centerCrop().into(employeeAvatar)
            Toast.makeText(this, "Name is $fullName", Toast.LENGTH_LONG).show()

        }
    }
}
