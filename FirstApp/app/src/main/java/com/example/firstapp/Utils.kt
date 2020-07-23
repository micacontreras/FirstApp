package com.example.firstapp

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun showDialog(
    context: Context,
    title: String,
    message: String,
    positiveButton: String,
    positiveAction: (() -> Unit)? = null,
    negativeButton: String? = null
) {
    AlertDialog.Builder(context).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButton) { _, _ -> positiveAction?.invoke() }
        setNegativeButton(negativeButton) { _, _ -> }
        create()
        show()
    }
}

fun defineMonth(month: Int): String {
    return when (month) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> "Error"
    }
}
