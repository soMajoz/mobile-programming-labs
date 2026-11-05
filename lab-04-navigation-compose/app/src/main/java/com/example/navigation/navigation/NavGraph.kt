package com.example.navigation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigation.screens.UserDetailScreen
import com.example.navigation.screens.UserListScreen

/**
 * Настраивает маршруты списка и профиля с целочисленным аргументом userId.
 *
 * @param modifier Модификатор контейнера навигации.
 */
@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = USER_LIST_ROUTE,
        modifier = modifier
    ) {
        composable(route = USER_LIST_ROUTE) {
            UserListScreen(
                onUserClick = { userId ->
                    navController.navigate("user_detail/$userId")
                }
            )
        }
        composable(
            route = USER_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(ARG_USER_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt(ARG_USER_ID) ?: INVALID_USER_ID
            UserDetailScreen(
                userId = userId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private const val USER_LIST_ROUTE = "user_list"
private const val USER_DETAIL_ROUTE = "user_detail/{userId}"
private const val ARG_USER_ID = "userId"
private const val INVALID_USER_ID = -1
