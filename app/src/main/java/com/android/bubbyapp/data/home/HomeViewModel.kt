package com.android.bubbyapp.data.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.android.bubbyapp.data.NavigationItem
import com.android.bubbyapp.navigation.PostOfficeAppRouter
import com.android.bubbyapp.navigation.Screen
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {


	private val TAG = HomeViewModel::class.simpleName

	val fullName: MutableLiveData<String> = MutableLiveData()

	val navigationItemsList = listOf<NavigationItem>()

	val isUserLoggedIn:
		MutableLiveData<Boolean> = MutableLiveData()

	fun logout() {

		val firebaseAuth = FirebaseAuth.getInstance()

		firebaseAuth.signOut()

		val authStateListener = FirebaseAuth.AuthStateListener {
			if (it.currentUser == null) {
				Log.d(TAG, "Inside sign outsuccess")
				PostOfficeAppRouter.navigateTo(Screen.LoginScreen)
			} else {
				Log.d(TAG, "Inside sign out is not complete")
			}
		}

		firebaseAuth.addAuthStateListener(authStateListener)

	}

	fun checkForActiveSession() {
		if (FirebaseAuth.getInstance().currentUser != null) {
			Log.d(TAG, "Valid session")
			isUserLoggedIn.value = true
		} else {
			Log.d(TAG, "User is not logged in")
			isUserLoggedIn.value = false
		}
	}


	val emailId: MutableLiveData<String> = MutableLiveData()

	fun getUserData() {
		FirebaseAuth.getInstance().currentUser?.also { user ->
			user.email?.also { email ->
				emailId.value = email
			}
		}
		val userId = FirebaseAuth.getInstance().currentUser?.uid
		val db = FirebaseFirestore.getInstance()
		val userRef = db.collection("users").document(userId ?: "")
		userRef.get()
			.addOnSuccessListener { documentSnapshot ->
				if (documentSnapshot.exists()) {
					val firstName = documentSnapshot.getString("firstName")
					val lastName = documentSnapshot.getString("lastName")
					val name = "$firstName $lastName"
					fullName.value = name
				}
			}
			.addOnFailureListener { e ->
				e.message?.let { Log.d("ERR", it) }
			}

	}

}