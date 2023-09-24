package com.android.bubbyapp.data.signup

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.android.bubbyapp.data.RegistrationUIState
import com.android.bubbyapp.data.rules.Validator
import com.android.bubbyapp.navigation.PostOfficeAppRouter
import com.android.bubbyapp.navigation.Screen


class SignupViewModel : ViewModel() {

	private val TAG = SignupViewModel::class.simpleName


	var registrationUIState = mutableStateOf(RegistrationUIState())

	var allValidationsPassed = mutableStateOf(false)

	var signUpInProgress = mutableStateOf(false)

	fun onEvent(event: SignupUIEvent) {
		when (event) {
			is SignupUIEvent.FirstNameChanged -> {
				registrationUIState.value = registrationUIState.value.copy(
					firstName = event.firstName
				)
				printState()
			}

			is SignupUIEvent.LastNameChanged -> {
				registrationUIState.value = registrationUIState.value.copy(
					lastName = event.lastName
				)
				printState()
			}

			is SignupUIEvent.EmailChanged -> {
				registrationUIState.value = registrationUIState.value.copy(
					email = event.email
				)
				printState()

			}


			is SignupUIEvent.PasswordChanged -> {
				registrationUIState.value = registrationUIState.value.copy(
					password = event.password
				)
				printState()

			}

			is SignupUIEvent.RegisterButtonClicked -> {
				signUp()
			}

			is SignupUIEvent.PrivacyPolicyCheckBoxClicked -> {
				registrationUIState.value = registrationUIState.value.copy(
					privacyPolicyAccepted = event.status
				)
			}
		}
		validateDataWithRules()
	}


	private fun signUp() {
		Log.d(TAG, "Inside_signUp")
		printState()
		createUserInFirebase(
			email = registrationUIState.value.email,
			password = registrationUIState.value.password,
			firstName = registrationUIState.value.firstName,
			lastName = registrationUIState.value.lastName
		)
	}

	private fun validateDataWithRules() {
		val fNameResult = Validator.validateFirstName(
			fName = registrationUIState.value.firstName
		)

		val lNameResult = Validator.validateLastName(
			lName = registrationUIState.value.lastName
		)

		val emailResult = Validator.validateEmail(
			email = registrationUIState.value.email
		)


		val passwordResult = Validator.validatePassword(
			password = registrationUIState.value.password
		)

		val privacyPolicyResult = Validator.validatePrivacyPolicyAcceptance(
			statusValue = registrationUIState.value.privacyPolicyAccepted
		)

		registrationUIState.value = registrationUIState.value.copy(
			firstNameError = fNameResult.status,
			lastNameError = lNameResult.status,
			emailError = emailResult.status,
			passwordError = passwordResult.status,
			privacyPolicyError = privacyPolicyResult.status
		)


		allValidationsPassed.value = fNameResult.status && lNameResult.status &&
			emailResult.status && passwordResult.status && privacyPolicyResult.status

	}

	private fun printState() {
		Log.d(TAG, "Inside_printState")
		Log.d(TAG, registrationUIState.value.toString())
	}


	private fun createUserInFirebase(
		email: String,
		password: String,
		firstName: String,
		lastName: String
	) {

		signUpInProgress.value = true

		FirebaseAuth
			.getInstance()
			.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener {
				Log.d(TAG, "Inside_OnCompleteListener")
				Log.d(TAG, " isSuccessful = ${it.isSuccessful}")

				signUpInProgress.value = false
				if (it.isSuccessful) {
					val user = FirebaseAuth.getInstance().currentUser
					val userId = user?.uid

					saveUserDataToFirestore(userId, firstName, lastName, email)
					PostOfficeAppRouter.navigateTo(Screen.HomeScreen)
				}
			}
			.addOnFailureListener {
				Log.d(TAG, "Inside_OnFailureListener")
				Log.d(TAG, "Exception= ${it.message}")
				Log.d(TAG, "Exception= ${it.localizedMessage}")
			}
	}

	private fun saveUserDataToFirestore(
		userId: String?,
		firstName: String,
		lastName: String,
		email: String
	) {
		val db = FirebaseFirestore.getInstance()
		val userRef = db.collection("users").document(userId ?: "")

		val userData = hashMapOf(
			"firstName" to firstName,
			"lastName" to lastName,
			"email" to email
		)

		userRef.set(userData)
			.addOnSuccessListener {
				Log.d(TAG, "saved successfully")
			}
			.addOnFailureListener { e ->
				Log.d(TAG, "error while saving user data")

			}
	}


}
