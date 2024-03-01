package com.example.lunimary.base

import androidx.lifecycle.MediatorLiveData

class LunimaryMediatorLiveData<T>(data: T): MediatorLiveData<T>(data) {
    override fun getValue(): T {
        return super.getValue()!!
    }
}