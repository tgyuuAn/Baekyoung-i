package com.tgyuu.baekyoung_i.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tgyuu.baekyoung_i.main.navigation.BaekyoungNavHost
import com.tgyuu.baekyoung_i.main.navigation.TopLevelDestination
import com.tgyuu.designsystem.theme.BaekyoungTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaekyoungTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        BaekyoungBottomBar(
                            currentRoute = currentRoute,
                            onNavigateToDestination = { destination ->
                                navigateToTopLevelDestination(
                                    navController,
                                    destination
                                )
                            }
                        )
                    }) { innerPadding ->
                    BaekyoungNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private fun navigateToTopLevelDestination(
    navController: NavController,
    destination: TopLevelDestination
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
) {
    BottomNavigation(
        backgroundColor = BaekyoungTheme.colors.white,
        modifier = modifier
    ) {
        TopLevelDestination.entries.forEach { destination ->
            BottomNavigationItem(
                selected = currentRoute == destination.route,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    destination.selectedIcon
                },
                label = {
                    Text(
                        text = stringResource(id = destination.titleTextId),
                        fontStyle = BaekyoungTheme.typography.contentNormal
                    )
                }
            )
        }
    }
}