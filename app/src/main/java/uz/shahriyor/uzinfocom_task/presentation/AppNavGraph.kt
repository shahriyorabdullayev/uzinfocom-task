package uz.shahriyor.uzinfocom_task.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yandex.mapkit.mapview.MapView
import kotlinx.serialization.Serializable
import uz.shahriyor.uzinfocom_task.R
import uz.shahriyor.uzinfocom_task.presentation.home.HomeScreen
import uz.shahriyor.uzinfocom_task.presentation.order.OrderScreen
import kotlin.reflect.KClass

sealed interface Route {
    @Serializable data object Home : Route
    @Serializable data object Order : Route
}

private data class TopLevelTab(
    val route: Route,
    val routeClass: KClass<*>,
    @StringRes val label: Int,
    @DrawableRes val icon: Int
)

private val topLevelTabs = listOf(
    TopLevelTab(Route.Home, Route.Home::class, R.string.tab_home, R.drawable.ic_home),
    TopLevelTab(Route.Order, Route.Order::class, R.string.tab_order, R.drawable.ic_order)
)

@Composable
fun AppNavGraph(mapView: MapView?) {
    val rootNavController = rememberNavController()
    val backStackEntry by rootNavController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = rootNavController,
            startDestination = Route.Home,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable<Route.Home> {
                HomeScreen(mapView = mapView)
            }
            composable<Route.Order> {
                OrderScreen(mapView = mapView)
            }
        }

        NavigationBar {
            topLevelTabs.forEach { tab ->
                val selected = currentDestination
                    ?.hierarchyChain()
                    ?.any { it.hasRoute(tab.routeClass) } == true
                NavigationBarItem(
                    selected = selected,
                    onClick = { rootNavController.navigateTopLevel(tab.route) },
                    icon = {
                        Icon(
                            painter = painterResource(tab.icon),
                            contentDescription = stringResource(tab.label)
                        )
                    },
                    label = { Text(stringResource(tab.label)) }
                )
            }
        }
    }
}

private fun NavController.navigateTopLevel(route: Route) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

private fun NavDestination.hierarchyChain(): Sequence<NavDestination> =
    generateSequence(this) { it.parent }
