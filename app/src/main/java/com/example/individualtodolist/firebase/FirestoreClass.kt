package com.example.individualtodolist.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.individualtodolist.activities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.example.individualtodolist.models.Category
import com.example.individualtodolist.models.User
import com.example.individualtodolist.utils.Constants


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }

    fun loadUserData(activity: Activity, readCategoriesList: Boolean = false) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())

                val loggedInUser = document.toObject(User::class.java)!!

                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting loggedIn user details",
                    e
                )
            }
    }

    fun getCategoryList(activity: MainActivity) {
        mFireStore.collection(Constants.CATEGORIES)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val categoryList: ArrayList<Category> = ArrayList()
                for (i in document.documents) {
                    val category = i.toObject((Category::class.java))!!
                    category.documentId = i.id
                    categoryList.add(category)
                }
                activity.populateCategoriesListToUI(categoryList)
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while creating a category", e)
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getCategoryDetails(activity: ToDoListActivity, documentId: String) {
        mFireStore.collection(Constants.CATEGORIES)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                var category = document.toObject(Category::class.java)!!
                category.documentId = document.id
                activity.categoryDetails(category)

            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while creating a category", e)
            }

    }

    fun createCategory(activity: CreateCategoryActivity, category: Category) {
        mFireStore.collection(Constants.CATEGORIES)
            .document()
            .set(category, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Category created successfully")
                Toast.makeText(
                    activity,
                    "Category created succesfully.", Toast.LENGTH_SHORT
                ).show()
                activity.categoryCreatedSuccesfully()

            }.addOnFailureListener { exception ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a category",
                    exception
                )
            }
    }

    fun addUpdateToDoList(activity: ToDoListActivity, category: Category) {
        val todoListHashMap = HashMap<String, Any>()
        todoListHashMap[Constants.TODO_LIST] = category.todoList

        mFireStore.collection(Constants.CATEGORIES)
            .document(category.documentId)
            .update(todoListHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Todos updated successfully")

                activity.addUpdateToDoListSuccess()
            }.addOnFailureListener { exception ->
                Log.e(activity.javaClass.simpleName, "Todos while creating a category")
            }
    }
}