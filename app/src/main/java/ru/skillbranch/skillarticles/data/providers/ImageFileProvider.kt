package ru.skillbranch.skillarticles.data.providers

import android.net.Uri
import androidx.core.content.FileProvider

/**
 * Created by Anna Shabaeva on 05.11.2020
 */

class ImageFileProvider : FileProvider() {
    override fun getType(uri: Uri): String? {
        return "image/jpeg"
    }
}