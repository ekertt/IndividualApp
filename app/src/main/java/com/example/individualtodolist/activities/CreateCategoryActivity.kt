package com.example.individualtodolist.activities

import activities.BaseActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.individualtodolist.R
import com.example.individualtodolist.firebase.FirestoreClass
import kotlinx.android.synthetic.main.activity_create_category.*
import com.example.individualtodolist.models.Category
import com.example.individualtodolist.utils.Constants

class CreateCategoryActivity : BaseActivity() {
    private lateinit var mUserName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        setupActionBar()

        if (intent.hasExtra(Constants.NAME)) {
            mUserName = intent.getStringExtra(Constants.NAME)
        }

        btn_create.setOnClickListener {
            createCategory()
        }
    }

    // Here the read storage permission result will be handled. And further execution will be done.)
    /**
     * This function will notify the user after tapping on allow or deny
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // Get the result of the image selection based on the constant code.)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun categoryCreatedSuccesfully() {
        setResult(Activity.RESULT_OK)

        finish()
    }

    private fun createCategory() {
        val assignedUsersArrayList: ArrayList<String> = ArrayList()
        assignedUsersArrayList.add(getCurrentUserId())

        var category = Category(
            et_category_name.text.toString(),
            assignedUsersArrayList
        )

        FirestoreClass().createCategory(this, category)
    }

    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_create_category_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_create_category_activity.setNavigationOnClickListener { onBackPressed() }
    }

}