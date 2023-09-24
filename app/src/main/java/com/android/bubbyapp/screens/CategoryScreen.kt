package com.android.bubbyapp.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.bubbyApp.R
import com.android.bubbyapp.components.AppToolbar
import com.android.bubbyapp.data.category.CategoryViewModel
import com.android.bubbyapp.model.Category
import com.android.bubbyapp.navigation.PostOfficeAppRouter
import com.android.bubbyapp.navigation.Screen
import com.android.bubbyapp.theme.Primary
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CategoryScreen(
	categoryViewModel: CategoryViewModel = viewModel(),
) {

	val id = categoryViewModel.getUserID()
	val viewModel = viewModel<CategoryViewModel>()
	val categoryState by viewModel.categoryState.collectAsState()

	val scaffoldState = rememberScaffoldState()
	val coroutineScope = rememberCoroutineScope()

	Scaffold(
		scaffoldState = scaffoldState,
		topBar = {
			TopAppBar(
				backgroundColor = Primary,
				title = { Text(text = "Create Category") },
				navigationIcon = {
					IconButton(onClick = {
						PostOfficeAppRouter.navigateTo(
							Screen.HomeScreen
						)
					}) {
						Icon(Icons.Default.ArrowBack, contentDescription = null)
					}
				},
				actions = {
					IconButton(
						onClick = {
							val category = id?.let {
								Category(
									userId = it, // Replace with the actual user ID
									title = categoryState.title,
									description = categoryState.description,
									totalAmount = categoryState.totalAmount.toFloat()
								)
							}
							if (category != null) {
								viewModel.createCategory(category)
							}
							coroutineScope.launch {
								scaffoldState.snackbarHostState.showSnackbar("Category created")
							}
						}
					) {
						Text("Save")
					}
				}
			)
		}
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
		) {
			OutlinedTextField(
				value = categoryState.title,
				onValueChange = { viewModel.onTitleChange(it) },
				label = { Text(text = "Title") },
				modifier = Modifier.fillMaxWidth()
			)
			Spacer(modifier = Modifier.height(16.dp))
			OutlinedTextField(
				value = categoryState.description,
				onValueChange = { viewModel.onDescriptionChange(it) },
				label = { Text(text = "Description") },
				modifier = Modifier.fillMaxWidth()
			)
			Spacer(modifier = Modifier.height(16.dp))
			OutlinedTextField(
				value = categoryState.totalAmount,
				onValueChange = { viewModel.onTotalAmountChange(it) },
				label = { Text(text = "Total Amount") },
				modifier = Modifier.fillMaxWidth()
			)
		}
	}
}

@Preview
@Composable
fun CategoryScreenPreview() {
	CategoryScreen()
}