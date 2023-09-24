package com.android.bubbyapp.app

import HomeScreen
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.bubbyapp.data.home.HomeViewModel
import com.android.bubbyapp.navigation.PostOfficeAppRouter
import com.android.bubbyapp.navigation.Screen
import com.android.bubbyapp.screens.LoginScreen
import com.android.bubbyapp.screens.SignUpScreen
import com.android.bubbyapp.screens.TermsAndConditionsScreen

@Composable
fun PostOfficeApp(homeViewModel: HomeViewModel = viewModel()) {

    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        if (homeViewModel.isUserLoggedIn.value == true) {
            PostOfficeAppRouter.navigateTo(Screen.HomeScreen)
        }

        Crossfade(targetState = PostOfficeAppRouter.currentScreen) { currentState ->
            when (currentState.value) {
                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }

                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }

                is Screen.LoginScreen -> {
                    LoginScreen()
                }

                is Screen.HomeScreen -> {
                   HomeScreen()
                }
            }
        }

    }
}