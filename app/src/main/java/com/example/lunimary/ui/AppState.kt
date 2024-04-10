package com.example.lunimary.ui

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.lunimary.base.currentUser
import com.example.lunimary.base.mmkv.DarkThemeSetting
import com.example.lunimary.base.mmkv.SettingMMKV
import com.example.lunimary.base.notLogin
import com.example.lunimary.base.pager.PageItem
import com.example.lunimary.models.Article
import com.example.lunimary.models.User
import com.example.lunimary.ui.common.ArticleNavArguments
import com.example.lunimary.ui.common.DEFAULT_WEB_URL
import com.example.lunimary.ui.common.EDIT_ARTICLE_KEY
import com.example.lunimary.ui.common.EDIT_TYPE_KEY
import com.example.lunimary.ui.common.PAGE_ARTICLE_ITEM_KEY
import com.example.lunimary.ui.common.PageArticleNavArguments
import com.example.lunimary.ui.common.RelationPageType
import com.example.lunimary.ui.common.UrlNavArguments
import com.example.lunimary.ui.common.WEB_VIEW_URL_KEY
import com.example.lunimary.ui.common.setNavViewUser
import com.example.lunimary.ui.common.setRelationPage
import com.example.lunimary.ui.edit.EditType
import com.example.lunimary.ui.login.UserViewModel
import com.example.lunimary.util.logd
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    userViewModel: UserViewModel,
    windowSizeClass: WindowSizeClass,
): LunimaryAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        windowSizeClass,
        userViewModel
    ) {
        LunimaryAppState(
            navController = navController,
            windowSizeClass = windowSizeClass,
            userViewModel = userViewModel,
            coroutineScope = coroutineScope
        )
    }
}

@Stable
class LunimaryAppState(
    val navController: NavHostController,
    private val windowSizeClass: WindowSizeClass,
    val userViewModel: UserViewModel,
    val coroutineScope: CoroutineScope
) {
    private val _selectedBottomTab = mutableStateOf(TopLevelDestination.Home)
    val selectedBottomTab: State<TopLevelDestination> get() = _selectedBottomTab

    fun updateSelectedBottomTab(newDestination: TopLevelDestination) {
        if (newDestination == selectedBottomTab.value) {
            return
        }
        "updateSelectedBottomTab: $newDestination".logd("TopLevelScreens")
        _selectedBottomTab.value = newDestination
    }

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean get() = !shouldShowBottomBar

    private val _darkThemeSettingState = mutableStateOf(
        if (SettingMMKV.userHasSetTheme) {
            SettingMMKV.darkThemeSetting
        } else {
            DarkThemeSetting.FollowSystem
        }
    )
    val darkThemeSettingState: State<DarkThemeSetting> get() = _darkThemeSettingState

    fun onThemeSettingChange(theme: DarkThemeSetting) {
        if (theme == SettingMMKV.darkThemeSetting) {
            return
        }
        SettingMMKV.darkThemeSetting = theme
        SettingMMKV.userHasSetTheme = true
        _darkThemeSettingState.value = theme
    }

    var lastBackClickedTime = System.currentTimeMillis()
    fun popBackStack() {
        if (System.currentTimeMillis() - lastBackClickedTime < 1000) {
            return
        }
        navController.popBackStack()
    }

    /**
     * @param fromNeedLogin
     * false -> system default
     * true -> our defined
     */
    fun navToLogin(fromNeedLogin: Boolean = false) {
        navController.navToLogin(fromNeedLogin)
    }

    fun navToHome(
        from: String
    ) {
        if (notLogin() && selectedBottomTab.value != TopLevelDestination.Home) {
            updateSelectedBottomTab(TopLevelDestination.Home)
        }
        navController.navToHomeWithFrom(from)
    }

    fun navToUser(
        from: String
    ) {
        updateSelectedBottomTab(TopLevelDestination.User)
        navToHome(from)
    }

    fun navToSettings() {
        navController.navigate(Screens.Settings.route)
    }

    fun navToRegister() {
        navController.navigate(Screens.Register.route)
    }

    fun navToEdit(
        editType: EditType = EditType.New,
        theArticle: PageItem<Article>? = null
    ) {
        "navToEdit: theArticle=$theArticle".logd("nav_article")
        navController.navToEdit(theArticle, editType)
    }

    fun navToDraft() {
        navController.navigate(Screens.Drafts.route)
    }

    fun navToWeb(url: String? = null) {
        navController.navToWeb(url)
    }

    fun navToBrowse(article: PageItem<Article>) {
        "navToBrowse: article=$article".logd("nav_article")
        navController.navToBrowse(article)
    }

    fun navToRelation(relationPageType: RelationPageType) {
        navController.navToRelation(relationPageType)
    }

    fun navToInformation() {
        navController.navToInformation()
    }

    fun navToSearch() {
        navController.navToSearch()
    }

    fun navToViewUser(
        user: User,
        from: String
    ) {
        navController.navToViewUser(user, from)
    }

    fun navToPrivacy() {
        navController.navigate(Screens.PrivacyProtocol.route)
    }

    private fun NavController.navToViewUser(
        user: User,
        from: String
    ) {
        if (user.id == currentUser.id) {
            updateSelectedBottomTab(TopLevelDestination.User)
            navToHome(from)
            return
        }
        setNavViewUser(user)
        navigate(Screens.ViewUser.route)
    }
}

private fun NavController.navToHomeWithFrom(from: String) {
    val navOptions = when (from) {
        Screens.Login.route -> {
            navOptions {
                popUpTo(Screens.Login.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        Screens.Search.route -> {
            navOptions {
                popUpTo(Screens.Search.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        Screens.Relation.route -> {
            navOptions {
                popUpTo(Screens.Relation.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        Screens.BrowseArticle.route -> {
            navOptions {
                popUpTo(Screens.BrowseArticle.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        Screens.BrowseArticle.route -> {
            navOptions {
                popUpTo(Screens.BrowseArticle.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        Screens.Settings.route -> {
            navOptions {
                popUpTo(Screens.Settings.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        Screens.ViewUser.route -> {
            navOptions {
                popUpTo(Screens.ViewUser.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        Screens.AddArticle.route -> {
            navOptions {
                popUpTo(Screens.AddArticle.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }


        Screens.Drafts.route -> {
            navOptions {
                popUpTo(Screens.Drafts.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        else -> navOptions { }
    }
    val route = HOME_ROUTE
    navigate(
        route = route,
        navOptions = navOptions
    )
}

private fun NavController.navToSearch() {
    navigate(Screens.Search.route)
}

private fun NavController.navToInformation() {
    navigate(Screens.Information.route)
}

private fun NavController.navToBrowse(article: PageItem<Article>) {
    PageArticleNavArguments[PAGE_ARTICLE_ITEM_KEY] = article
    navigate(BROWSE_ARTICLE_ROOT)
}

fun NavController.navToLogin(fromNeedLogin: Boolean = false) {
    navigate("$LOGIN_ROOT/$fromNeedLogin")
}

private fun NavController.navToRelation(relationPageType: RelationPageType) {
    setRelationPage(relationPageType)
    navigate(Screens.Relation.route)
}

private fun NavController.navToEdit(
    theArticle: PageItem<Article>?,
    editType: EditType
) {
    var article = theArticle
    if (theArticle == null) {
        article = PageItem(Article(id = -10000))
    }
    ArticleNavArguments[EDIT_ARTICLE_KEY] = article!!
    ArticleNavArguments[EDIT_TYPE_KEY] = editType
    navigate(ADD_ARTICLE_ROOT)
}

private fun NavController.navToWeb(url: String? = null) {
    UrlNavArguments[WEB_VIEW_URL_KEY] = url ?: DEFAULT_WEB_URL
    navigate(WEB_VIEW_ROOT)
}

@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, arguments ->
            Log.v(
                "navigation_events",
                "Foods Destination:${destination.route.toString()},arguments:$arguments"
            )
            val entries = navController.visibleEntries.value
            Log.v("navigation_events", "visibleEntries=$entries")
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}