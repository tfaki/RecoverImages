package com.lofty.recover.images.adapter.module

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.lofty.recover.images.R
import com.lofty.recover.images.adapter.model.ImageModel

fun ImageModule(oneAdapterClickListener: oneAdapterClickListener, context: Context) : ItemModule<ImageModel> = object : ItemModule<ImageModel>() {
    override fun onBind(item: Item<ImageModel>, viewBinder: ViewBinder) {
        val imageView = viewBinder.findViewById<ImageView>(R.id.imageView)
        val imageTv = viewBinder.findViewById<TextView>(R.id.imageTv)
        val container = viewBinder.findViewById<MaterialCardView>(R.id.cv)

        val myBitmap = BitmapFactory.decodeFile(item.model.path + "/" + item.model.title)
        //imageView.setImageBitmap(myBitmap)

        Glide.with(context)
            .asBitmap()
            .load(myBitmap)
            .into(imageView)

        imageTv.text = item.model.title

        container.setOnClickListener {
            oneAdapterClickListener.onItemClick(it, item.model, item.model.path + "/" + item.model.title, myBitmap, imageView)
        }

    }

    override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig(){
        override fun withLayoutResource() = R.layout.rv_image_item
    }

}

interface oneAdapterClickListener{
    fun onItemClick(view: View, model: ImageModel, url: String, bitmap: Bitmap, imageView: ImageView){}
}