package com.example.mccpushnotificationassignment

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var mRequestQueue: RequestQueue
    val URL_SINGUP = "https://mcc-users-api.herokuapp.com/add_new_user"
    val URL_LOGIN = "https://mcc-users-api.herokuapp.com/login"
    private var isLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, LoginActivity::class.java))

        // initialize the RequestQueue
        mRequestQueue = Volley.newRequestQueue(this)

        // when user click on "btnSubmit" call "submit" function
        /*btnSubmit.setOnClickListener {
            submit()
        }*/

        // to switch between authentication state "Login" & "SignUp"
        /*btnAuthState.setOnClickListener {
            if (isLogin)
            {
                isLogin = false
                btnAuthState.text = "Login Instead"
                btnSubmit.text = "SignUp"
                txtFirstName.visibility = View.VISIBLE
                txtSecondName.visibility = View.VISIBLE
            } else {
                isLogin = true
                btnAuthState.text = "Sign Up Instead"
                btnSubmit.text = "Login"
                txtFirstName.visibility = View.GONE
                txtSecondName.visibility = View.GONE
            }
        }*/

    }

    // this function handles the input fields and call the right method "login OR signUp"
    /*private fun submit()
    {
        val fName = txtFirstName.text.toString().trim()
        val sName = txtSecondName.text.toString().trim()
        val email = txtEmailAddress.text.toString().trim()
        val password = txtPassword.text.toString().trim()

        if (isLogin)
        {
            if (email.isNotEmpty() && password.isNotEmpty())
            {
                login(email, password)
                txtEmailAddress.text.clear()
                txtPassword.text.clear()
            } else {
                Toast.makeText(this, "Fill Fields First!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            if (fName.isNotEmpty() && sName.isNotEmpty() &&
                email.isNotEmpty() && password.isNotEmpty())
            {
                signUp(fName, sName, email, password)
                txtFirstName.text.clear()
                txtSecondName.text.clear()
                txtEmailAddress.text.clear()
                txtPassword.text.clear()
            } else {
                Toast.makeText(this, "Fill Fields First!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/

    // this function to check user email and password
    private fun login(email: String, password: String)
    {
        val stringRequest = object : StringRequest(
            Method.POST, URL_LOGIN,
            Response.Listener { response ->
                try {
                    val jo = JSONObject(response)
                    val fName = jo.getJSONObject("data")["first_name"].toString()
                    val sName = jo.getJSONObject("data")["second_name"].toString()
                    val uEmail = jo.getJSONObject("data")["email"].toString()

                    val intent = Intent(this, SignUpActivity()::class.java)
                    startActivity(intent.apply {
                        putExtra("status", "login")
                        putExtra("firstName", fName)
                        putExtra("secondName", sName)
                        putExtra("email", uEmail)
                    })
                } catch (exception: Exception) {
                    Log.e("abd", "success catch : ${exception.message!!}")
                }
            },
            Response.ErrorListener {
                showMyDialog()
            })
        {
            override fun getParams(): MutableMap<String, String> {
                val userData = HashMap<String, String>()
                userData["email"] = email
                userData["password"] = password
                return userData
            }
        }

        // add the request to the queue
        mRequestQueue.add(stringRequest)
    }

    // this function to create new user
    private fun signUp(fName: String, sName: String, email: String, password: String)
    {
        val stringRequest = object : StringRequest(
            Method.POST, URL_SINGUP,
            Response.Listener { response ->
                try {
                    val jo = JSONObject(response)
                    val uFName = jo.getJSONObject("data")["first_name"].toString()
                    val uSName = jo.getJSONObject("data")["second_name"].toString()
                    val uEmail = jo.getJSONObject("data")["email"].toString()

                    val intent = Intent(this, SignUpActivity()::class.java)
                    startActivity(intent.apply {
                        putExtra("status", "signUp")
                        putExtra("firstName", uFName)
                        putExtra("secondName", uSName)
                        putExtra("email", uEmail)
                    })
                } catch (exception: Exception) {
                    Log.e("abd", exception.message!!)
                }
            },
            Response.ErrorListener {
                showMyDialog()
            })
        {
            override fun getParams(): MutableMap<String, String> {
                val userData = HashMap<String, String>()
                userData["firstName"] = fName
                userData["secondName"] = sName
                userData["email"] = email
                userData["password"] = password
                return userData
            }
        }

        // add the request to the queue
        mRequestQueue.add(stringRequest)
    }

    // this function to show dialog for the user to show his information
    private fun showMyDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Something went wrong!!")
        alertDialog.setMessage("Error in email or password, please try again later")
        alertDialog.setCancelable(true)
        alertDialog.setIcon(R.drawable.ic_info)
        alertDialog.setPositiveButton("Ok"){ dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.create().show()
    }

}
