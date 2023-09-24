import androidx.compose.foundation.background

import com.android.bubbyApp.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.bubbyapp.components.AppToolbar
import com.android.bubbyapp.components.CategoryCard
import com.android.bubbyapp.components.NavigationDrawerBody
import com.android.bubbyapp.components.NavigationDrawerHeader
import com.android.bubbyapp.data.category.CategoryViewModel
import com.android.bubbyapp.data.home.HomeViewModel
import com.android.bubbyapp.model.Category
import com.android.bubbyapp.navigation.PostOfficeAppRouter
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
	homeViewModel: HomeViewModel = viewModel(),
	categoryViewModel: CategoryViewModel = viewModel()
) {
	val id = categoryViewModel.getUserID()
	homeViewModel.getUserData()
	var categories by remember { mutableStateOf<List<Category>>(emptyList()) }

	val scaffoldState = rememberScaffoldState()
	val coroutineScope = rememberCoroutineScope()

	LaunchedEffect(id) {
		val loadedCategories = id?.let { categoryViewModel.loadCategories(userId = it) }
		if (loadedCategories != null) {
			categories = loadedCategories
		}
	}
	Scaffold(
		scaffoldState = scaffoldState,
		topBar = {
			AppToolbar(toolbarTitle = stringResource(id = R.string.hello),
				logoutButtonClicked = {
					homeViewModel.logout()
				},
				navigationIconClicked = {
					coroutineScope.launch {
						scaffoldState.drawerState.open()
					}
				}
			)
		},
		drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
		drawerContent = {
			NavigationDrawerHeader(homeViewModel.fullName.value)
			NavigationDrawerBody(navigationDrawerItems = homeViewModel.navigationItemsList,
				onNavigationItemClicked = {
					PostOfficeAppRouter.navigateTo(it.toNavigate)
				}
			)
		}
	) { paddingValues ->
		Surface(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.White)
				.padding(paddingValues)
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
			) {
				Text(
					text = "Categories",
					style = MaterialTheme.typography.h4,
					modifier = Modifier.padding(8.dp)
				)
				LazyVerticalGrid(
					columns = GridCells.Fixed(2), // 2 columns
					modifier = Modifier.fillMaxSize()
				) {
					items(categories) { category ->
						CategoryCard(
							category = category,
							color = getRandomColor(customColors)
						)
					}
				}
			}
		}
	}

}


val customColors = listOf(
	Color(0xFFADD9E0),
	Color(0xFFFEECDA),
	Color(0xFFCFBEDE),
	Color(0xFF9886C3),
	Color(0xFFDAB9B2),
	Color(0xffff7c97),
	Color(0xFF609657),
	Color(0xFF459E97),
)

fun getRandomColor(colors: List<Color>): Color {
	val randomIndex = (colors.indices).random()
	return colors[randomIndex]
}

@Preview
@Composable
fun HomeScreenPreview() {
	HomeScreen()
}

