package com.example.mccpushnotificationassignment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {
            val data = "{" +
                    "\"email\"" + ":" + "\"" + email_login.text.toString() + "\"," +
                    "\"password\"" + ":" + "\"" + password_login.text.toString() + "\"" +
                    "}"
            login(data)
        }

        signup_text.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }

    private fun login(data: String) {
        val URL = "https://mcc-users-api.herokuapp.com/login"
        requestQueue = Volley.newRequestQueue(applicationContext)
        Log.e("emd", "requestQueue: $requestQueue")
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL, Response.Listener { response ->
                try {
                    val objres = JSONObject(response)
                    Log.d("emd", "onResponse: $objres")
                    showUserData(
                        objres.getJSONObject("data")["first_name"].toString(),
                        objres.getJSONObject("data")["second_name"].toString(),
                        objres.getJSONObject("data")["email"].toString()
                    )
                } catch (e: JSONException) {
                    Log.d("emd", "Server Error ");
                }
            }, Response.ErrorListener { error ->
                Log.d("emd", "onErrorResponse: $error")
            })
        {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray {
                return data.toByteArray(charset("utf-8"))
            }
        }
        requestQueue.add(stringRequest)
    }

    fun showUserData(firstName: String, secondName: String, email: String) {
        AlertDialog.Builder(this)
            .setTitle("User Data")
            .setMessage("First Name: $firstName\nSecond Name: $secondName\nEmail: $email")
            .setPositiveButton("Close"){ dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }

}