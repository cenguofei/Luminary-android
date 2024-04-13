package com.example.lunimary.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lunimary.LunimaryApplication
import com.example.lunimary.base.network.NetworkMonitor
import com.example.lunimary.base.network.NetworkMonitorImpl
import com.example.lunimary.base.network.isCurrentlyConnected
import com.example.lunimary.util.logd
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class NetworkViewModel : ApiViewModel() {
    private val isOnline = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LunimaryApplication.applicationContext.isCurrentlyConnected(),
        )

    private val _online: MutableLiveData<Boolean> =
        MutableLiveData(LunimaryApplication.applicationContext.isCurrentlyConnected())
    val online: LiveData<Boolean> get() = _online

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
     * 当多个网络减少一个时
     *
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
        if (onSwitchToHaveNetMap.containsKey(key)) {
            return
        }
        "registerOnHaveNetwork:$key".logd("pagingKey")
        onSwitchToHaveNetMap[key] = noNetToHaveNet
    }

    fun unregisterOnHaveNetwork(key: String) {
        if (onSwitchToHaveNetMap.containsKey(key)) {
            "unregisterOnHaveNetwork:$key".logd("pagingKey")
            onSwitchToHaveNetMap.remove(key)
        }
    }

    fun unregisterOnHaveNetwork(vararg keys: String) {
        keys.forEach { unregisterOnHaveNetwork(it) }
    }

    fun registerOnHaveNetwork(
        keys: List<Pair<String, () -> Unit>>
    ) {
        keys.forEach {
            val key = it.first
            val callback = it.second
            registerOnHaveNetwork(key, callback)
        }
    }

    private fun dispatchOnHaveNetEvent() {
        onSwitchToHaveNetMap.values.forEach { it() }
    }

    companion object {
        private val networkMonitor: NetworkMonitor =
            NetworkMonitorImpl(LunimaryApplication.applicationContext)
    }
    init { collectNetState() }
}