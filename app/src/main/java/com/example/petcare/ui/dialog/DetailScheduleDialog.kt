package com.example.petcare.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.petcare.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class DetailScheduleDialog : DialogFragment() {

    private val args: DetailScheduleDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val data = args.detail
        val title = data.name?.ifEmpty { "No Title" }

        val desc = if (data.description!!.isNotEmpty()) {
            "\nNote : ${data.description}"
        } else ""
        val ps = if (data.postScript!!.isNotEmpty()) {
            "\n*ps : ${data.postScript}"
        } else ""

        val format = SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm.", Locale.getDefault())
        val time = format.format(Date(data.time!!))
        return MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog_rounded)
            .setTitle(title)
            .setMessage("You Have $title in $time $desc $ps")
            .setPositiveButton("OK", null)
            .create()
    }

}