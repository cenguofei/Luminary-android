package com.example.lunimary

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.lunimary.models.ktor.httpClient
import com.example.lunimary.models.ktor.securityPost
import com.example.lunimary.models.source.UserRepository
import com.example.lunimary.util.isForegroundStr
import com.example.lunimary.util.logv
import com.example.lunimary.util.onlineStatusPath
import com.tencent.mmkv.MMKV
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.parameters
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
    }
}

class ActivityLifeCycleCallbacks: Application.ActivityLifecycleCallbacks {
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
    }

    override fun onActivityPaused(activity: Activity) {
        activityStack.remove(activity)
        if (activityStack.isEmpty()) {
            reportOnlineStatus(false)
        }
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val userRepository = UserRepository()

    private fun reportOnlineStatus(isForeground: Boolean = false) {
        coroutineScope.launch(Dispatchers.IO) {
            val deferred = async { userRepository.checkIsLogin() }
            val response = deferred.await()
            if (response.data == true) {
                httpClient.securityPost(urlString = onlineStatusPath) {
                    header(checkIsLoginHeader, "true")
                    setBody(FormDataContent(parameters { append(isForegroundStr, "$isForeground") }))
                }
            }
        }
    }
}

const val checkIsLoginHeader = "checkIsLogin"