package net.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import net.basicmodel.R
import net.entity.ResourceEntity

class ResourceManager {
    companion object {
        private var instance: ResourceManager? = null
            get() {
                field?.let { } ?: run {
                    field = ResourceManager()
                }
                return field
            }

        @Synchronized
        fun get(): ResourceManager {
            return instance!!
        }
    }

    fun getResource(context: Context): ArrayList<String> {
        return getEditorItemRes(context)
    }


    private fun getEditorItemRes(context: Context): ArrayList<String> {
        val result = ArrayList<String>()
        result.add(res2String(context, R.mipmap.pip))
        result.add(res2String(context, R.mipmap.color))
        result.add(res2String(context, R.mipmap.bokeh))
        result.add(res2String(context, R.mipmap.pixel))
        result.add(res2String(context, R.mipmap.shattering))
        return result
    }

    fun res2String(context: Context, id: Int): String {
        val r = context.resources
        val uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + r.getResourcePackageName(id) + "/"
                    + r.getResourceTypeName(id) + "/"
                    + r.getResourceEntryName(id)
        )
        return uri.toString()
    }

    fun getPipMaskRes(
        context: Context,
        clazz: Class<*>,
        folderName: String,
        filter: String
    ): ArrayList<ResourceEntity> {
        return getResourceByFolder(context, clazz, folderName, filter)
    }

    fun getResourceByFolder(
        context: Context,
        clazz: Class<*>,
        folderName: String,
        filter: String
    ): ArrayList<ResourceEntity> {
        val result = ArrayList<ResourceEntity>()
        for (field in clazz.fields) {
            val name = field.name
            if (name.startsWith(filter)) {
                val id = context.resources.getIdentifier(name, folderName, context.packageName)
                val entity = ResourceEntity(name, id)
                result.add(entity)
            }
        }
        return result
    }
}