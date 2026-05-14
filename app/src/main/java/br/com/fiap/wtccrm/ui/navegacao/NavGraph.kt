package br.com.fiap.wtccrm.ui.navegacao

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import br.com.fiap.wtccrm.ui.screen.*


@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("chat") {
            ChatScreen(navController = navController)
        }
    }
}