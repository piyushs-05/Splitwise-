package com.example.splitwise_final.data.local

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simple in-memory storage for group IDs
 * In a production app, this would be Room database or SharedPreferences
 */
@Singleton
class GroupCache @Inject constructor() {
    private val groupIds = mutableSetOf<String>()

    fun addGroupId(groupId: String) {
        groupIds.add(groupId)
    }

    fun getGroupIds(): List<String> {
        return groupIds.toList()
    }

    fun hasGroups(): Boolean {
        return groupIds.isNotEmpty()
    }

    fun clear() {
        groupIds.clear()
    }
}

