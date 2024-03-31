package com.example.lunimary.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunimary.LunimaryApplication
import com.example.lunimary.base.network.NetworkMonitor
import com.example.lunimary.base.network.NetworkMonitorImpl
import com.example.lunimary.base.network.isCurrentlyConnected
import com.example.lunimary.util.logd
import com.example.lunimary.util.loge
import com.example.lunimary.util.unknownErrorMsg
import io.ktor.util.collections.ConcurrentSet
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val processingMap = ConcurrentSet<String>()
    fun fly(url: String, action: () -> Unit) {
        if (url !in processingMap) {
            action()
        }
    }

    fun land(url: String) {
        if (url in processingMap) {
            processingMap.remove(url)
        }
    }

    private val isOnline = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LunimaryApplication.applicationContext.isCurrentlyConnected(),
        )

    private val _online: MutableLiveData<Boolean> =
        MutableLiveData(LunimaryApplication.applicationContext.isCurrentlyConnected())
    val online: LiveData<Boolean> get() = _online

    init { collectNetState() }

    private fun collectNetState() {
        viewModelScope.launch {
            isOnline.collectLatest { nowIsOnline ->
                val preOnline = online.value!!
                "nowIsOnline=$nowIsOnline, preOnline=$preOnline".logd("App_is_off_line")
                if (preOnline) {
                    if (!nowIsOnline) { // 有网 -> 无网
                        _online.postValue(false)
                    }
                } else {
                    if (nowIsOnline) { // 无网 -> 有网
                        _online.postValue(true)
                        onHaveNetwork()
                        dispatchOnHaveNetEvent()
                    }
                }
            }
        }
    }

    /**
     * 当从无网络状态变为有网络时
     * two net to one net
     *
     * online=true
     *
     * nowIsOnline=false, preOnline=true
     *
     * nowIsOnline=false, preOnline=true
     *
     * nowIsOnline=true, preOnline=false
     *
     * nowIsOnline=true, preOnline=false
     *
     * online=true
     *
     * 即使有网也会经过 true -> false -> true，所以使用时应该更具实际情况再次判断是否应该请求网络
     */
    open fun onHaveNetwork() { }

    private val onSwitchToHaveNetMap = mutableMapOf<String, () -> Unit>()
    fun registerOnHaveNetwork(
        key: String,
        noNetToHaveNet: () -> Unit
    ) {
        "set noNetToHaveNet".logd("noNetToHaveNet")
        if (onSwitchToHaveNetMap.containsKey(key)) {
            return
        }
        onSwitchToHaveNetMap[key] = noNetToHaveNet
    }

    fun unregisterOnHaveNetwork(key: String) {
        if (onSwitchToHaveNetMap.containsKey(key)) {
            onSwitchToHaveNetMap.remove(key)
        }
    }

    private fun dispatchOnHaveNetEvent() {
        onSwitchToHaveNetMap.values.forEach { it() }
    }

    fun <T> request(
        onSuccess: (data: T?, msg: String?) -> Unit = { _, _ -> },
        onFailed: (msg: String) -> Unit = {},
        onFinish: () -> Unit = {},
        block: suspend () -> BaseResponse<T>
    ): Job {
        return viewModelScope.launch {
            runCatching {
                block()
            }.onSuccess { response ->
                "request success: response=$response".logd()
                runCatching {
                    if (response.isSuccess()) {
                        onSuccess(response.data, response.msg)
                        response.data ?: run {
                            "data is null.".logd()
                        }
                    } else {
                        onFailed(response.msg)
                    }
                }.onFailure {
                    onFailed(it.message ?: unknownErrorMsg)
                }
                onFinish()
            }.onFailure {
                it.message?.loge()
                it.printStackTrace()
                onFailed(it.message ?: unknownErrorMsg)
                onFinish()
            }
        }
    }

    companion object {
        private val networkMonitor: NetworkMonitor =
            NetworkMonitorImpl(LunimaryApplication.applicationContext)
    }
}

object ScopeViewModel : BaseViewModel()