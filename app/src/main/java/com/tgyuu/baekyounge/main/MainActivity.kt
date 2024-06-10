package com.tgyuu.baekyounge.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event.SCREEN_VIEW
import com.google.firebase.analytics.FirebaseAnalytics.Param.SCREEN_NAME
import com.google.firebase.analytics.analytics
import com.tgyuu.baekyounge.main.navigation.BaekyoungNavHost
import com.tgyuu.baekyounge.main.navigation.TopLevelDestination
import com.tgyuu.chatting.ai.navigation.aiChattingNavigationRoute
import com.tgyuu.designsystem.component.BaekyoungButton
import com.tgyuu.designsystem.theme.BaekyoungTheme
import com.tgyuu.feature.auth.navigation.authNavigationRoute
import com.tgyuu.feature.auth.signup.navigation.signUpNavigationRoute
import com.tgyuu.feature.chatting.mentoring.navigation.mentorChattingNavigationRoute
import com.tgyuu.feature.mentee.navigation.findMentorNavigationRoute
import com.tgyuu.feature.mentoring.mentee.navigation.mentoringMenteeNavigationRoute
import com.tgyuu.feature.mentoring.mentor.navigation.mentoringMentorNavigationRoute
import com.tgyuu.feature.profile.setting.navigation.settingNavigationRoute
import com.tgyuu.feature.shop.navigation.shopNavigationRoute
import com.tgyuu.feature.splash.navigation.splashNavigationRoute
import com.tgyuu.feature.storage.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var networkObserver: NetworkObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarTransParent()

        firebaseAnalytics = Firebase.analytics

        setContent {
            BaekyoungTheme {
                val navController = rememberNavController()

                logScreenView(navController, firebaseAnalytics)
                val networkState by networkObserver.networkState.collectAsStateWithLifecycle()

                when (networkState) {
                    NetworkState.CONNECTED -> Log.d("test", "Network가 연결되었습니다.")
                    NetworkState.NOT_CONNECTED -> {
                        Dialog(onDismissRequest = { }) {
                            Card(shape = RoundedCornerShape(10.dp)) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .background(BaekyoungTheme.colors.white)
                                        .padding(vertical = 16.dp, horizontal = 20.dp),
                                ) {
                                    Text(
                                        text = "네트워크 상태를 확인 해주세요.",
                                        style = BaekyoungTheme.typography.labelBold.copy(fontSize = 14.sp),
                                        modifier = Modifier.padding(bottom = 2.dp),
                                    )

                                    Text(
                                        text = "네트워크 연결에 실패하였습니다. 다시 시도하시겠어요?",
                                        style = BaekyoungTheme.typography.labelRegular.copy(fontSize = 10.sp),
                                        color = BaekyoungTheme.colors.red,
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp),
                                    ) {
                                        BaekyoungButton(
                                            text = R.string.cancel,
                                            buttonColor = BaekyoungTheme.colors.grayF2,
                                            textColor = BaekyoungTheme.colors.black,
                                            onButtonClick = { },
                                            modifier = Modifier.weight(1f),
                                        )

                                        BaekyoungButton(
                                            text = R.string.complete,
                                            textColor = BaekyoungTheme.colors.white,
                                            buttonColor = BaekyoungTheme.colors.red,
                                            onButtonClick = { },
                                            modifier = Modifier.weight(1f),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }

                Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        var bottomBarState by rememberSaveable { mutableStateOf(false) }

                        handleBottomBarState(
                            currentRoute,
                            setBottomBarState = { boolean ->
                                bottomBarState = boolean
                            },
                        )

                        BaekyoungBottomBar(
                            currentRoute = currentRoute,
                            bottomBarState = bottomBarState,
                            onNavigateToDestination = { destination ->
                                navigateToTopLevelDestination(
                                    navController,
                                    destination,
                                )
                            },
                            modifier = Modifier.height(60.dp),
                        )
                    },
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                ) { innerPadding ->
                    BaekyoungNavHost(
                        navController = navController,
                        modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkObserver.unsubscribeNetworkCallback()
    }
}

private fun logScreenView(
    navController: NavController,
    firebaseAnalytics: FirebaseAnalytics,
) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        val params = Bundle()
        params.putString(SCREEN_VIEW, destination.route)
        firebaseAnalytics.logEvent(SCREEN_NAME, params)
    }
}

private fun handleBottomBarState(
    currentRoute: String?,
    setBottomBarState: (Boolean) -> Unit,
): Unit = when (currentRoute) {
    null -> setBottomBarState(false)
    splashNavigationRoute -> setBottomBarState(false)
    authNavigationRoute -> setBottomBarState(false)
    aiChattingNavigationRoute() -> setBottomBarState(false)
    signUpNavigationRoute() -> setBottomBarState(false)
    settingNavigationRoute -> setBottomBarState(false)
    mentoringMenteeNavigationRoute -> setBottomBarState(false)
    findMentorNavigationRoute -> setBottomBarState(false)
    mentorChattingNavigationRoute() -> setBottomBarState(false)
    mentoringMentorNavigationRoute -> setBottomBarState(false)
    else -> setBottomBarState(true)
}

private fun navigateToTopLevelDestination(
    navController: NavController,
    destination: TopLevelDestination,
) {
    navController.navigate(route = destination.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
internal fun BaekyoungBottomBar(
    modifier: Modifier = Modifier,
    currentRoute: String?,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    bottomBarState: Boolean,
) {
    AnimatedVisibility(
        visible = bottomBarState,
        label = "",
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            modifier = modifier,
        ) {
            TopLevelDestination.entries.forEach { destination ->
                if ((currentRoute == com.tgyuu.feature.home.navigation.homeNavigationRoute) &&
                    (destination.route == com.tgyuu.feature.home.navigation.homeNavigationRoute)
                ) {
                    return@forEach
                }

                if ((currentRoute != com.tgyuu.feature.home.navigation.homeNavigationRoute) &&
                    (destination.route == shopNavigationRoute)
                ) {
                    return@forEach
                }

                val isSelect = currentRoute == destination.route
                val unselectedContentColor =
                    if (currentRoute == com.tgyuu.feature.home.navigation.homeNavigationRoute) {
                        BaekyoungTheme.colors.blueFB
                    } else {
                        BaekyoungTheme.colors.gray95
                    }

                BottomNavigationItem(
                    selected = isSelect,
                    modifier = Modifier.background(Color.Transparent),
                    onClick = { onNavigateToDestination(destination) },
                    selectedContentColor = BaekyoungTheme.colors.black,
                    unselectedContentColor = unselectedContentColor,
                    icon = {
                        Icon(
                            painter = painterResource(id = destination.selectedIcon),
                            contentDescription = null,
                        )
                    },
                )
            }
        }
    }
}

private fun ComponentActivity.setSystemBarTransParent() = enableEdgeToEdge()
