package com.example.firstapp

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun showDialog(context: Context, title: String, message: String, positiveButton: String, positiveAction: (() -> Unit)? = null, negativeButton:String?= null ){
    AlertDialog.Builder(context).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButton) { _, _ -> positiveAction?.invoke() }
        setNegativeButton(negativeButton){_, _ -> }
        create()
        show()
    }
}