package com.example.lunimary

import android.app.Activity
import android.app.Application
import android.os.Bundle
import coil.Coil
import coil.ImageLoader
import com.example.lunimary.models.source.remote.repository.UserRepository
import com.example.lunimary.storage.MMKVKeys
import com.example.lunimary.util.boolean
import com.example.lunimary.util.logv
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.LinkedList

class LuminaryApplication : Application() {
    private lateinit var activityLifecycleCallbacks: ActivityLifecycleCallbacks
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this).logv("mmkv")
        activityLifecycleCallbacks = ActivityLifeCycleCallbacks()
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        APP_CONTEXT = this
        initCoil()
    }


    private fun initCoil() {
        val imageLoader = ImageLoader.Builder(context = this)
            .build()
        Coil.setImageLoader(imageLoader)
    }

    companion object {

        private var APP_CONTEXT: Application? = null
        val applicationContext: Application get() = APP_CONTEXT!!
    }
}

class ActivityLifeCycleCallbacks : Application.ActivityLifecycleCallbacks {
    private val activityStack = LinkedList<Activity>()
    private var reported = false
    private var firstUseTheApp by boolean(key = MMKVKeys.FIRST_USE_APP)

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
//        if (isFirst) {
//            checkLogin(
//                coroutineScope,
//                userRepository,
//                isLogin = {
//                    httpClient.securityPost(urlString = onlineStatusPath) {
//                        url {
//                            appendPathSegments("$isForeground")
//                        }
//                    }
//                }
//            )
//            isFirst = false
//        } else {
//            if (!notLogin()) {
//                coroutineScope.launch {
//                    httpClient.securityPost(urlString = onlineStatusPath) {
//                        url {
//                            appendPathSegments("$isForeground")
//                        }
//                    }
//                }
//            }
//        }
    }
}


const val checkIsLoginHeader = "checkIsLogin"