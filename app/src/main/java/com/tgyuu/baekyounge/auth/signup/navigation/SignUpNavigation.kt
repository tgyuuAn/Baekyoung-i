package com.tgyuu.baekyounge.auth.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.tgyuu.baekyounge.auth.signup.SignUpRoute

const val signUpNavigationRoute = "sign_up_route"

fun NavController.navigateToSignUp(navOptions: NavOptions? = navOptions {}) {
    this.navigate(signUpNavigationRoute, navOptions)
}

fun NavGraphBuilder.signUpScreen(navigateToHome: () -> Unit) {
    composable(route = signUpNavigationRoute) {
        SignUpRoute(navigateToHome)
    }
}