package com.example.lunimary.base


open class LRUCache<T>(private val capacity: Int = 16) :
    LinkedHashMap<String, T>(capacity, 0.75f, true) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, T>?): Boolean {
        return size > capacity
    }
}
