package com.example.petcare.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.petcare.BuildConfig
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import java.io.File
import java.text.SimpleDateFormat
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

object GeneratePostId{
    fun postIdRandom(): String = kotlin.collections.List(20){
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")
}


object ShareLink{
    fun generateShareLink(
        deepLink: Uri,
        previewImgLink: Uri,
        getShareableLink: (String) -> Unit ={}
    ){
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink().run {
            //? to get link parameter
            link = deepLink

            //?domain prefix is domain name on dynamic link firebase dashboard
            domainUriPrefix = BuildConfig.PREFIX

            //?pass preview image here
            setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setImageUrl(previewImgLink)
                    .build()
            )

            androidParameters {
                build()
            }

            buildDynamicLink()

        }

        val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
            link = deepLink
            domainUriPrefix = BuildConfig.PREFIX

            //?pass preview image here
            setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setImageUrl(previewImgLink)
                    .build()
            )

            androidParameters {
                build()
            }

            buildDynamicLink()

        }.addOnSuccessListener {
            getShareableLink.invoke(it.shortLink.toString())
        }

    }
}