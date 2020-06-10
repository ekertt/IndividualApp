package com.example.individualtodolist.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.individualtodolist.R
import com.example.individualtodolist.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*
import com.example.individualtodolist.models.User

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupActionBar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name: String = et_name.text.toString()
        val email: String = et_email.text.toString()
        val password: String = et_password.text.toString()

        if (formValidation(name, email, password)) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        FirestoreClass().registerUser(this, user)
                    } else {
                        Toast.makeText(
                            this,
                            task.exception!!.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun formValidation(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Missing an email")
                false
            }
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Missing a username")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Missing a password")
                false
            }
            else -> {
                true
            }
        }

    }

    fun userRegisteredSuccess() {
        Toast.makeText(
            this,
            "you have succesfully registered", Toast.LENGTH_LONG
        ).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}
