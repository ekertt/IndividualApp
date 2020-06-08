package com.example.individualtodolist.activities

import activities.BaseActivity
import com.example.individualtodolist.adapters.CategoryItemsAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.individualtodolist.R
import com.example.individualtodolist.firebase.FirestoreClass
import kotlinx.android.synthetic.main.app_bar_main.*
import com.example.individualtodolist.models.Category
import com.example.individualtodolist.models.User
import com.example.individualtodolist.utils.Constants
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity() {

    companion object {
        const val CREATE_CATEGORY_REQUEST_CODE: Int = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()

        FirestoreClass().loadUserData(this, true)

        FirestoreClass().getCategoryList(this)

        fab_create_category.setOnClickListener {
            val intent = Intent(
                this,
                CreateCategoryActivity::class.java
            )
            startActivityForResult(intent, CREATE_CATEGORY_REQUEST_CODE)
        }
    }

    fun categoriesListToView(categoryList: ArrayList<Category>) {

        if (categoryList.size > 0) {
            rv_categories_list.visibility = View.VISIBLE
            tv_no_categories_available.visibility = View.GONE

            rv_categories_list.layoutManager = LinearLayoutManager(this)
            rv_categories_list.setHasFixedSize(true)

            val adapter = CategoryItemsAdapter(this, categoryList)
            rv_categories_list.adapter = adapter

            adapter.setOnClickListener(object : CategoryItemsAdapter.OnClickListener {
                override fun onClick(position: Int, model: Category) {
                    val intent = Intent(this@MainActivity, ToDoListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })

        } else {
            rv_categories_list.visibility = View.GONE
            tv_no_categories_available.visibility = View.VISIBLE
        }
    }

    private fun setupActionBar() {

        FirestoreClass().getCurrentUsername().addOnSuccessListener { document ->
            val loggedInUser = document.toObject(User::class.java)!!

            tv_username.text = "Hello ${loggedInUser.name}!"
        }
        setSupportActionBar(toolbar_main_activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == CREATE_CATEGORY_REQUEST_CODE
        ) {
            FirestoreClass().getCategoryList(this)

        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }
}
