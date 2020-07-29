package com.example.firstapp.task.db

import android.content.ContentValues
import android.provider.BaseColumns
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


/*@Entity(tableName = TasksEntity.TABLE_NAME)
data class TasksEntity2(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "taskName") val taskName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "startDate") val startDate: Date,
    @ColumnInfo(name = "startTime") val startTime: Date,
    @ColumnInfo(name = "colorEvent") val colorEvent: String,
    @ColumnInfo(name = "colorEventInt") val colorEventInt: Int
)*/

@Entity(tableName = TasksEntity.TABLE_NAME)
class TasksEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    var id: Long = 0L,
    @ColumnInfo(name = COLUMN_NAME)
    var taskName: String? = null,
    @ColumnInfo(name = COLUMN_DESCRIPCION)
    var description: String? = null,
    @ColumnInfo(name = COLUMN_START_DATE)
    var startDate: Date = Date(),
    @ColumnInfo(name = COLUMN_START_TIME)
    var startTime: Date = Date(),
    @ColumnInfo(name = COLUMN_COLOR_EVENT)
    var colorEvent: String? = null,
    @ColumnInfo(name = COLUMN_COLOR_EVENT_INT)
    var colorEventInt: Int? = null)
{
    companion object {
        const val TABLE_NAME = "TasksEntity"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "taskName"
        const val COLUMN_DESCRIPCION = "description"
        const val COLUMN_START_DATE = "startDate"
        const val COLUMN_START_TIME = "startTime"
        const val COLUMN_COLOR_EVENT = "colorEvent"
        const val COLUMN_COLOR_EVENT_INT = "colorEventInt"


        fun fromContentValues(@Nullable values: ContentValues?): TasksEntity {
            val task = TasksEntity()
            if (values != null && values.containsKey(COLUMN_ID)) {
                task.id = values.getAsLong(COLUMN_ID)
            }
            if (values != null && values.containsKey(COLUMN_DESCRIPCION)) {
                task.description = values.getAsString(COLUMN_DESCRIPCION)
            }
            if (values != null && values.containsKey(COLUMN_START_DATE)) {
                task.startDate = Converters().fromTimestamp(values.getAsLong(COLUMN_START_DATE))!!

            }
            if (values != null && values.containsKey(COLUMN_START_TIME)) {
                task.startTime = Converters().fromTimestamp(values.getAsLong(COLUMN_START_TIME))!!
            }
            if (values != null && values.containsKey(COLUMN_COLOR_EVENT)) {
                task.colorEvent = values.getAsString(COLUMN_COLOR_EVENT)
            }
            if (values != null && values.containsKey(COLUMN_COLOR_EVENT_INT)) {
                task.colorEventInt = values.getAsInteger(COLUMN_COLOR_EVENT_INT)
            }

            return task
        }
    }
}
