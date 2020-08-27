package com.lofty.recover.images.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.lofty.recover.images.R
import com.lofty.recover.images.adapter.model.ImageModel
import com.lofty.recover.images.adapter.module.ImageModule
import com.lofty.recover.images.adapter.module.oneAdapterClickListener
import com.loftymr.loftyalertdialog.AlertTypeState
import com.loftymr.loftyalertdialog.LoftyAlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.io.*

private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

class MainActivity : BaseActivity() {

    private lateinit var oneAdapter: OneAdapter
    private val list = mutableListOf<Diffable>()
    lateinit var manager: ReviewManager
    private var reviewInfo: ReviewInfo? = null
    private var downloadImageCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initReviews()
        toolbar.titleView.text = "Recover Images"

        val dir = Environment.getExternalStorageDirectory()
        val path = dir.absolutePath

        rv.layoutManager = GridLayoutManager(this, 2)

        oneAdapter = OneAdapter(rv)
            .attachItemModule(ImageModule(object : oneAdapterClickListener {
                override fun onItemClick(
                    view: View,
                    model: ImageModel,
                    url: String,
                    bitmap: Bitmap,
                    imageView: ImageView
                ) {
                    LoftyAlertDialog.Builder(this@MainActivity)
                        .alertType(AlertTypeState.WARNING)
                        .setChangeable(true)
                        .setCancelable(true)
                        .colorId(R.color.deep_orange)
                        .headerTitle("WARNING")
                        .isTwoButton(true)
                        .alertMessage("Are you sure to download this ?")
                        .rightButtonText("Yes")
                        .leftButtonText("No")
                        .rightButtonAction {
                            downloadImage(imageView)
                        }
                        .show()
                }
            }, this))

        askCameraPermission {
            val fileTrgt = File(path)
            var subTarget2: File?

            val file: Array<File> = fileTrgt.listFiles()

            for (element in file) {
                if (element.name.length > 2) {
                    val substr: String = element.name.substring(element.name.length - 3)
                    if (substr == "jpg" || substr == "jpeg" || substr == "png") {
                        list.add(ImageModel(element.name, fileTrgt.absolutePath))
                    }

                    if (!element.list().isNullOrEmpty()) {
                        element.list().forEach { element1 ->
                            val subFileTrgt = File("$path/${element.name}/$element1") //

                            val subFile = subFileTrgt.listFiles()
                            if (!subFile.isNullOrEmpty()) {
                                for (item in subFile) {
                                    subTarget2 = File("$path/${element.name}/$element1")
                                    if (item.name.length > 2) {
                                        val substr: String =
                                            item.name.substring(item.name.length - 3)
                                        if (substr == "jpg" || substr == "jpeg" || substr == "png") {
                                            list.add(
                                                ImageModel(
                                                    item.name,
                                                    subTarget2!!.absolutePath
                                                )
                                            )
                                        } else {
                                            val targetSubSub =
                                                File("$path/${element.name}/$element1/${item.name}") //
                                            val subSubTarget = targetSubSub.listFiles()
                                            if (!subSubTarget.isNullOrEmpty()) {
                                                for (it1 in subSubTarget) {
                                                    if (it1.name.length > 2) {
                                                        val substr: String =
                                                            it1.name.substring(it1.name.length - 3)
                                                        if (substr == "jpg" || substr == "jpeg" || substr == "png") {
                                                            list.add(
                                                                ImageModel(
                                                                    it1.name,
                                                                    targetSubSub.absolutePath
                                                                )
                                                            )
                                                        } else {
                                                            val sub3Target =
                                                                File("$path/${element.name}/$element1/${item.name}/${it1.name}")
                                                            val target3Sub = sub3Target.listFiles()
                                                            if (!target3Sub.isNullOrEmpty()) {
                                                                for (ite in target3Sub) {
                                                                    if (ite.name.length > 2) {
                                                                        val substr: String =
                                                                            ite.name.substring(ite.name.length - 3)
                                                                        if (substr == "jpg" || substr == "jpeg" || substr == "png") {
                                                                            list.add(
                                                                                ImageModel(
                                                                                    ite.name,
                                                                                    sub3Target.absolutePath
                                                                                )
                                                                            )
                                                                        } else {
                                                                            val sub4Target =
                                                                                File("$path/${element.name}/$element1/${item.name}/${it1.name}/${ite.name}")
                                                                            val target4Sub =
                                                                                sub4Target.listFiles()
                                                                            if (!target4Sub.isNullOrEmpty()) {
                                                                                for (itt in target4Sub) {
                                                                                    if (itt.name.length > 2) {
                                                                                        val substr: String =
                                                                                            itt.name.substring(
                                                                                                itt.name.length - 3
                                                                                            )
                                                                                        if (substr == "jpg" || substr == "jpeg" || substr == "png") {
                                                                                            list.add(
                                                                                                ImageModel(
                                                                                                    itt.name,
                                                                                                    sub4Target.absolutePath
                                                                                                )
                                                                                            )
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            oneAdapter.setItems(list)
        }
    }

    fun downloadImage(view: ImageView) {
        val bitmapDrawable: BitmapDrawable = view.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap

        var outStream: FileOutputStream? = null
        val sdCard = Environment.getExternalStorageDirectory()
        val dir = File(sdCard.absolutePath.toString() + "/Recover Images")
        dir.mkdirs()
        val fileName =
            String.format("%d.jpg", System.currentTimeMillis())
        val outFile = File(dir, fileName)
        outStream = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()

        Toast.makeText(this, "Has been downloaded", Toast.LENGTH_LONG).show()

        downloadImageCount = +1

        if (downloadImageCount % 5 == 0) {
            askForReview()
        }

    }

    // Call this method asap, for example in onCreate()
    private fun initReviews() {
        manager = ReviewManagerFactory.create(this)
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                reviewInfo = request.result
            } else {
                // Log error
            }
        }
    }

    // Call this when you want to show the dialog
    private fun askForReview() {
        if (reviewInfo != null) {
            manager.launchReviewFlow(this, reviewInfo!!).addOnFailureListener {
                // Log error and continue with the flow
            }.addOnCompleteListener { _ ->
                // Log success and continue with the flow
            }
        }
    }

    private fun askCameraPermission(onGranted: () -> Unit) {
        if (cameraPermissionGranted()) {
            onGranted()
        } else {
            askPermission(
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            ) {
                onGranted()
            }.onDeclined {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun cameraPermissionGranted() =
        arrayOf(
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        ).all {
            ContextCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
        }
}