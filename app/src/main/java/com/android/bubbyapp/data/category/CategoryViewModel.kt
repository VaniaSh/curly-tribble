package com.android.bubbyapp.data.category

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.bubbyapp.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

data class CategoryState(
	var title: String = "",
	var description: String = "",
	var totalAmount: String = ""
)

val UID: MutableLiveData<String> = MutableLiveData()

class CategoryViewModel : ViewModel() {
	private val _categoryState = MutableStateFlow(CategoryState())
	val categoryState = _categoryState.asStateFlow()

	fun onTitleChange(title: String) {
		_categoryState.value = _categoryState.value.copy(title = title)
	}

	fun getUserID(): String? {
		val userId = FirebaseAuth.getInstance().currentUser?.uid
		return userId.also { UID.value = it }
	}

	fun onDescriptionChange(description: String) {
		_categoryState.value = _categoryState.value.copy(description = description)
	}

	fun onTotalAmountChange(totalAmount: String) {
		_categoryState.value = _categoryState.value.copy(totalAmount = totalAmount)
	}

	private val db = FirebaseFirestore.getInstance()

	suspend fun loadCategories(userId: String): MutableList<Category> {
		val categoriesList = mutableListOf<Category>()

		try {
			val querySnapshot = db.collection("categories")
				.whereEqualTo("userId", userId)
				.get()
				.await()

			for (document in querySnapshot) {
				val category = document.toObject(Category::class.java)
				categoriesList.add(category)
			}
		} catch (e: Exception) {
			Log.d("ERROR", "${e}")
		}

		return categoriesList
	}

	fun createCategory(category: Category) {
		val db = FirebaseFirestore.getInstance()

		// Create a new category document with a unique ID
		val newCategoryRef = db.collection("categories").document()

		// Set the user ID for the category
		category.copy(documentId = newCategoryRef.id).let { newCategory ->
			newCategoryRef.set(newCategory)
				.addOnSuccessListener {
					Log.d("CREATE", "Category creation was successful")
				}
				.addOnFailureListener { exception ->
					Log.d("ERROR", "error")
				}
		}
	}

}

