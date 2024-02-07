package com.devay.wid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devay.wid.screens.addEditScreen.AddEditScreen
import com.devay.wid.screens.homeScreen.HomeScreen
import com.devay.wid.ui.theme.WIDTheme
import com.devay.wid.util.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WIDTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Route.HomeScreen
                ) {
                    composable(
                        route = Route.HomeScreen,
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(500)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(500)
                            )
                        }
                    ) {
                        HomeScreen( onNavigate = {
                            navController.navigate(it.route)
                        })
                    }
                    composable(
                        route = Route.AddEditScreen + "?taskId={taskId}",
                        arguments = listOf(
                            navArgument(name = "taskId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        ),
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(500)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(500)
                            )
                        }
                    ) {
                        AddEditScreen(popBackStack = { navController.popBackStack() })
                    }
                }

            }
        }
    }
}