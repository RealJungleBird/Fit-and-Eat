package su.junglebird.fiteat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import su.junglebird.fiteat.ui.navigation.*
import su.junglebird.fiteat.ui.screens.*
import su.junglebird.fiteat.ui.theme.FitEatTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            FitEatTheme {
                Main()
            }
        }
    }
}

@Composable
fun Navigation(paddingValues: PaddingValues, navController: NavHostController)
{
    val paddingValues2 = PaddingValues(
        start = 16.dp, end = 16.dp,
        top=paddingValues.calculateTopPadding(),
        bottom=paddingValues.calculateBottomPadding())
    NavHost(
        navController = navController,
        startDestination = Routes.Profile.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        modifier = Modifier.padding(paddingValues2)) {
        composable(route = Routes.Profile.route) { Profile() }
        composable(Routes.MyDishes.route) { MyDishes() }
        composable(Routes.DaySummary.route) { DaySummary() }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()

    val topLevelRoutes = listOf<TopLevelRoutes>(
        TopLevelRoutes("Сводка дня", Routes.DaySummary.route, Icons.Outlined.Home),
        TopLevelRoutes("Мои блюда", Routes.MyDishes.route, Icons.Outlined.Menu),
        TopLevelRoutes("Профиль",Routes.Profile.route,Icons.Outlined.Person),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val appBarState = getAppBarState(currentRoute)

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
        },
        topBar = @OptIn(ExperimentalMaterial3Api::class){
            TopAppBar(title = {Text(appBarState.title)})
        }

    ) { innerPadding ->
        Navigation(innerPadding, navController)
    }
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