package su.junglebird.fiteat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import su.junglebird.fiteat.ui.navigation.*
import su.junglebird.fiteat.ui.screens.*
import su.junglebird.fiteat.ui.theme.FitEatTheme
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()



            val topLevelRoutes = listOf<TopLevelRoutes>(
                TopLevelRoutes("Сводка дня", Routes.DaySummary.routes, Icons.Outlined.Home),
                TopLevelRoutes("Мои блюда", Routes.MyDishes.routes, Icons.Outlined.Menu),
                TopLevelRoutes("Профиль",Routes.Profile.routes,Icons.Outlined.Person),
            )

            FitEatTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            topLevelRoutes.forEach {topLevelRoute ->
                                BottomNavigationItem(
                                    selected = currentDestination?.route == topLevelRoute.routes,
                                    onClick = {
                                        navController.navigate(topLevelRoute.routes){
                                            popUpTo(navController.graph.findStartDestination().id){
                                                saveState = true
                                            }

                                            launchSingleTop = true
                                            restoreState = true
                                        }


                                    },
                                    icon = { Icon(topLevelRoute.icon,"иконка") },
                                    label = { Text(text = topLevelRoute.name)}
                                )
                            }
                        }
                    }



                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = Routes.Profile.routes) {
                        composable(Routes.Profile.routes) { Profile() }
                        composable(Routes.MyDishes.routes) { MyDishes() }
                        composable(Routes.DaySummary.routes) { DaySummary() }
                    }

//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}

@Composable
fun Main() {


//    val topLevelRoutes = listOf<TopLevelRoutes>(
//        TopLevelRoutes("Newsfeed",Routes.Newsfeed.routes, Icons.Outlined.Menu),
//        TopLevelRoutes("Profile",Routes.Profile.routes, Icons.Outlined.Person),
//        TopLevelRoutes("Settings",Routes.Settings.routes, Icons.Outlined.Settings)
//    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FitEatTheme {
        Greeting("Android")
    }
}