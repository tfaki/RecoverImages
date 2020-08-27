package com.lofty.recover.images.adapter.model

import com.idanatz.oneadapter.external.interfaces.Diffable

data class ImageModel(val title: String, val path: String) : Diffable {
    override fun getUniqueIdentifier() = title.hashCode().toLong()

    override fun areContentTheSame(other: Any) = other is ImageModel &&
            title == other.title &&
            path == other.path
}