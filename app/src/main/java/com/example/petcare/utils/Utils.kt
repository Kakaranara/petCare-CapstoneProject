package com.example.petcare.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * * Insert Utility function here
 */

object AuthUtil {
    fun isLoginValid(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            throw Exception("Mail and Password should not be empty!")
        }
    }

    fun isRegisterValid(email: String, password: String, confirmPassword: String, name: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            throw Exception("Please input all the field!")
        }
        if (password != confirmPassword) {
            throw Exception("Password did not match")
        }
    }
}

object StoryUtil{
    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    val timestamp: String = SimpleDateFormat(
        FILENAME_FORMAT, Locale.US
    ).format(System.currentTimeMillis())

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timestamp, ".jpg", storageDir)
    }


}

object DateFormatter{

    fun formatterDate(currentDate: Long): String{
        val formatter = SimpleDateFormat("dd MMM, yyyy")
        val result = Date(currentDate)
        return formatter.format(result)
    }
}