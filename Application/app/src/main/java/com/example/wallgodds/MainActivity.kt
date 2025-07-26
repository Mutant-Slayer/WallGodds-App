package com.example.wallgodds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wallgodds.models.MainScreenEvent
import com.example.wallgodds.navigation.CustomNavigationBar
import com.example.wallgodds.navigation.Routes
import com.example.wallgodds.navigation.listOfNavItems
import com.example.wallgodds.ui.theme.AppPadding
import com.example.wallgodds.ui.theme.AppSize
import com.example.wallgodds.ui.theme.WallGoddsTheme
import com.example.wallgodds.utils.LazyRowScrollbar
import com.example.wallgodds.utils.RandomWallpaperGrid
import com.example.wallgodds.utils.WallGoddsSnackbar
import com.example.wallgodds.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WallGoddsTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val snackbarHostState = remember { SnackbarHostState() }
                val showLikeSnackBar by remember { mainViewModel.likeWallpaperSnackBarEnabled }.collectAsStateWithLifecycle(
                    true
                )

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                            WallGoddsSnackbar(message = data.visuals.message)
                        }
                    },
                    floatingActionButton = {
                        if (navBackStackEntry?.destination?.route in listOfNavItems.map { it.route }) {
                            Box(
                                modifier = Modifier.offset(y = 16.dp)
                            ) {
                                CustomNavigationBar(
                                    currentDestination = currentDestination,
                                    items = listOfNavItems,
                                    onItemClick = { item ->
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.home_page,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.favorites_page) {
                            HomePage(
                                snackbarHostState = snackbarHostState,
                                showLikeSnackBar = showLikeSnackBar,
                                onEvent = mainViewModel::onEvent
                            )
                        }
                        composable(Routes.home_page) {
                            HomePage(
                                snackbarHostState = snackbarHostState,
                                showLikeSnackBar = showLikeSnackBar,
                                onEvent = mainViewModel::onEvent
                            )
                        }
                        composable(Routes.upload_page) {
                            HomePage(
                                snackbarHostState = snackbarHostState,
                                showLikeSnackBar = showLikeSnackBar,
                                onEvent = mainViewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomePage(
    snackbarHostState: SnackbarHostState,
    showLikeSnackBar: Boolean,
    onEvent: (MainScreenEvent) -> Unit,
) {

    val wallpapers = List(50) {
        R.drawable.sample_wallpaper
    }

    val listState = rememberLazyListState()

    LaunchedEffect(showLikeSnackBar) {
        if (showLikeSnackBar) {
            snackbarHostState.showSnackbar(
                message = "Double Tap to Like Wallpaper",
                duration = SnackbarDuration.Short
            )
            onEvent(MainScreenEvent.DisableLikeWallpaperSnackBar)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppPadding.MainContentPadding, vertical = AppPadding.Medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.wallgodds_icon),
                contentDescription = "WallGodds Icon",
                modifier = Modifier.size(AppSize.IconMedium)
            )
            Image(
                painter = painterResource(R.drawable.profile_icon),
                contentDescription = "Profile Icon",
                modifier = Modifier.size(AppSize.IconMedium)
            )
        }
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = AppPadding.MainContentPadding),
            horizontalArrangement = Arrangement.spacedBy(AppPadding.PaddingBetweenCategories)
        ) {
            listOf(
                "Abstract",
                "Nature",
                "Anime",
                "Art",
                "Movies",
                "Vehicles",
                "Sports",
                "Games",
                "Travel",
                "Spiritual",
                "Music",
                "AIGen",
            ).forEach {
                item {
                    Box(
                        modifier = Modifier
                            .height(AppSize.CategoryPreview)
                            .aspectRatio(3f / 1f)
                            .clip(RoundedCornerShape(AppSize.LowCornerRadius))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.category_abstract),
                            contentDescription = "Book Cover",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = AppSize.FontSizeSmall,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        LazyRowScrollbar(
            listState = listState,
            indicatorColor = Color.Black,
            indicatorHeight = AppSize.indicatorHeight,
            indicatorWidth = AppSize.indicatorWidth,
            trackColor = Color.Gray,
            trackHeight = AppSize.trackHeight,
            trackWidth = AppSize.trackWidth,
            cornerRadius = AppSize.HighCornerRadius,
            verticalPadding = AppPadding.Medium
        )
        RandomWallpaperGrid(wallpapers = wallpapers)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    WallGoddsTheme {
        HomePage(
            snackbarHostState = snackbarHostState,
            showLikeSnackBar = true,
            onEvent = {}
        )
    }
}