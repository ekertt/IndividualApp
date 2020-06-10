package com.example.individualtodolist.activities

import android.app.Activity
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