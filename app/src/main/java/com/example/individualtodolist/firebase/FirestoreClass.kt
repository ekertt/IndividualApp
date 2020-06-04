package com.example.individualtodolist.firebase

import activities.*
import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.example.individualtodolist.models.Board
import com.example.individualtodolist.models.User
import com.example.individualtodolist.utils.Constants


/**
 * A custom class where we will add the operation performed for the firestore database.
 */
class FirestoreClass {

    // Create a instance of Firebase Firestore
    private val mFireStore = FirebaseFirestore.getInstance()

    /**
     * A function to make an entry of the registered user in the firestore database.
     */

    fun registerUser(activity: SignUpActivity, userInfo: User) {

        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
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

    fun getBoardList(activity: MainActivity){
        //CHECK AMONG ALL THE EXISTING BOARDS AND FIND THOSE ONE THAT WHERE ASSIGNED TO THAT SPECIFIC ID
        //(EITHER CREATED OR ASSGIGNED)
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for (i in document.documents){
                    //iterate through the document and include every single board to my board list
                    val board = i.toObject((Board::class.java))!!
                    board.documentId = i.id
                    boardList.add(board)
                }
                activity.populateBoardsListToUI(boardList)
            }.addOnFailureListener{e ->
                Log.e(activity.javaClass.simpleName, "Error while creating a board", e)
            }
    }

    /**
     * A function to SignIn using firebase and get the user details from Firestore Database.
     */
    fun loadUserData(activity: Activity, readBoardsList:Boolean = false) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val loggedInUser = document.toObject(User::class.java)!!

                // START
                // Here call a function of base activity for transferring the result to it.
                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                }
            }
            .addOnFailureListener {
                    e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting loggedIn user details",
                    e
                )
            }
    }

    /**
     * A function for getting the user id of current logged user.
     */
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getBoardDetails(activity: TaskListActivity, documentId: String){
        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                var board = document.toObject(Board::class.java)!!
                board.documentId = document.id
                activity.boardDetails(board)

            }.addOnFailureListener{e ->
                Log.e(activity.javaClass.simpleName, "Error while creating a board", e)
            }

    }

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Board created successfully")
                Toast.makeText(activity,
                    "Board created succesfully.", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccesfully()

            }.addOnFailureListener{
                exception ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board",
                    exception
                )
            }
    }
    fun addUpdateTaskList(activity: TaskListActivity, board: Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "TaskList updated successfully")

                activity.addUpdateTaskListSuccess()
            }.addOnFailureListener{
                exception ->
                Log.e(activity.javaClass.simpleName, "TaskList while creating a board")
            }
    }
}