package com.lofty.recover.images.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lofty.recover.images.R
import java.lang.ref.WeakReference

open class BaseActivity : AppCompatActivity(){
    private var progressDialog: WeakReference<AlertDialog?>? = null

    fun showLoading() {
        hideLoading()
        progressDialog = WeakReference(
            AlertDialog.Builder(this)
                .setView(R.layout.progress_bar)
                .setCancelable(true).create()
        )
        val dialog: AlertDialog? = progressDialog?.get()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
    }

    fun hideLoading() {
        if (progressDialog != null) {
            val dialog: AlertDialog? = progressDialog?.get()
            dialog?.dismiss()
            progressDialog = null
        }
    }

}