package com.example.firstapp.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.firstapp.task.db.TaskDataBase
import com.example.firstapp.task.db.TasksDao
import com.example.firstapp.task.db.TasksEntity


class TaskProvider : ContentProvider() {

    private var taskDao: TasksDao? = null
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert")
        when (uriMatcher.match(uri)) {
            ID_TASK_DATA -> {
                if (context != null) {
                    val id = taskDao?.insertProvider(TasksEntity.fromContentValues(values))
                    if (id != 0L) {
                        context!!.contentResolver
                            .notifyChange(uri, null)
                        return ContentUris.withAppendedId(uri, id!!)
                    }
                }
                throw IllegalArgumentException("Invalid URI: Insert failed$uri")
            }
            ID_TASK_DATA_ITEM -> throw IllegalArgumentException("Invalid URI: Insert failed$uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query")
        val cursor: Cursor
        when (uriMatcher.match(uri)) {
            ID_TASK_DATA -> {
                cursor = taskDao?.selectAll()!!
                if (context != null) {
                    cursor.setNotificationUri(
                        context?.contentResolver, uri
                    )
                    return cursor
                }
                throw IllegalArgumentException("Unknown URI: $uri")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun onCreate(): Boolean {
        taskDao = TaskDataBase.getDatabase(context!!).taskDao()
        return false
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d(TAG, "update")
        var count = 0
        when (uriMatcher.match(uri)) {
            ID_TASK_DATA -> { }
            ID_TASK_DATA_ITEM ->
                if (context != null) {
                    Log.d("Update", "Update method was called" )
                    val context = context ?: return 0
                    val task: TasksEntity = TasksEntity.fromContentValues(values)
                    task.id = ContentUris.parseId(uri)
                    TaskDataBase.getDatabase(context).taskDao().insert2(task)
                    context.contentResolver.notifyChange(uri, null)
                }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "delete")
        when (uriMatcher.match(uri)) {
            ID_TASK_DATA -> throw IllegalArgumentException("Invalid uri: cannot delete")
            ID_TASK_DATA_ITEM -> {
                if (context != null) {
                    Log.d("Delete", "Delete method was called")
                    return 0
                }
                throw IllegalArgumentException("Unknown URI:$uri")
            }
            else -> throw IllegalArgumentException("Unknown URI:$uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    init {
        uriMatcher.addURI(AUTHORITY, TASKS_ENTITY_TABLE_NAME, ID_TASK_DATA)
        uriMatcher.addURI(AUTHORITY, "$TASKS_ENTITY_TABLE_NAME/*", ID_TASK_DATA_ITEM)
    }


    companion object {
        const val TAG = "TaskProvider"
        const val AUTHORITY = "com.example.firstapp.provider"
        const val TASKS_ENTITY_TABLE_NAME = "TasksEntity"
        const val ID_TASK_DATA = 1
        const val ID_TASK_DATA_ITEM = 2
        val URI = Uri.parse("content://" + AUTHORITY + "/" + TASKS_ENTITY_TABLE_NAME)

    }

}