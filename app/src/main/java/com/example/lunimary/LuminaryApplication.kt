package com.example.lunimary

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.source.remote.UserRepository
import com.example.lunimary.util.UserState
import com.example.lunimary.util.logd
import com.example.lunimary.util.logv
import com.example.lunimary.util.notLogin
import com.example.lunimary.util.onlineStatusPath
import com.tencent.mmkv.MMKV
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.LinkedList

class LuminaryApplication : Application() {
    private val activityLifecycleCallbacks = ActivityLifeCycleCallbacks()
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this).logv("mmkv")
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        APP_CONTEXT = this
    }

    companion object {

        private var APP_CONTEXT: Application? = null
        val applicationContext: Application get() = APP_CONTEXT!!
    }
}

class ActivityLifeCycleCallbacks : Application.ActivityLifecycleCallbacks {
    private val activityStack = LinkedList<Activity>()
    private var reported = false

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        activityStack.addLast(activity)
        if (!reported) {
            reported = true
            reportOnlineStatus(true)
        }
        checkLogin(
            coroutineScope,
            userRepository,
            isLogin = { UserState.updateLoginState(true, "onActivityResumed") },
            logout = { UserState.updateLoginState(false, "onActivityResumed") }
        )
    }

    override fun onActivityPaused(activity: Activity) {
        activityStack.remove(activity)
        if (activityStack.isEmpty()) {
            reportOnlineStatus(false)
        }
        reported = false
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val userRepository = UserRepository()

    private var isFirst = true
    private fun reportOnlineStatus(isForeground: Boolean = false) {
        if (isFirst) {
            checkLogin(
                coroutineScope,
                userRepository,
                isLogin = {
                    httpClient.securityPost(urlString = onlineStatusPath) {
                        url {
                            appendPathSegments("$isForeground")
                        }
                    }
                }
            )
            isFirst = false
        } else {
            if (!notLogin()) {
                coroutineScope.launch {
                    httpClient.securityPost(urlString = onlineStatusPath) {
                        url {
                            appendPathSegments("$isForeground")
                        }
                    }
                }
            }
        }
    }
}

private fun checkLogin(
    coroutineScope: CoroutineScope,
    userRepository: UserRepository,
    isLogin: suspend () -> Unit = {},
    logout: suspend () -> Unit = {}
) {
    coroutineScope.launch(Dispatchers.IO) {
        val deferred = async { userRepository.checkIsLogin() }
        val response = deferred.await()
        "检查登录状态：response=${response.data.toString() + response.code + response.msg}".logd()
        if (response.data?.isLogin == true) {
            isLogin()
        } else {
            logout()
        }
    }
}

const val checkIsLoginHeader = "checkIsLogin"