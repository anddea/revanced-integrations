package app.revanced.integrations.music.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import app.revanced.integrations.music.utils.ReVancedUtils.getContext

@SuppressLint("DiscouragedApi")
object ResourceUtils {
    @JvmStatic
    fun identifier(name: String, type: ResourceType) =
        identifier(name, type, getContext())

    @JvmStatic
    val resources: Resources get() = getContext().resources

    @JvmStatic
    fun identifier(name: String, type: ResourceType, context: Context): Int {
        return resources.getIdentifier(name, type.value, context.packageName)
    }

    @JvmStatic
    fun string(name: String) = identifier(name, ResourceType.STRING).let {
        if (it == 0) name else resources.getString(it)
    }

    @JvmStatic
    fun integer(name: String) = resources.getInteger(identifier(name, ResourceType.INTEGER))

    @JvmStatic
    fun <R : View> findView(view: View, name: String): R {
        return view.findViewById(identifier(name, ResourceType.ID)) ?: run {
            throw IllegalArgumentException("View with name $name not found")
        }
    }

    @JvmStatic
    fun <R : View> findView(activity: Activity, name: String): R {
        return findView(activity.window.decorView, name)
    }
}

@Suppress("unused")
enum class ResourceType(internal val value: String) {
    ANIM("anim"),
    ARRAY("array"),
    ATTR("attr"),
    COLOR("color"),
    DIMEN("dimen"),
    DRAWABLE("drawable"),
    FONT("font"),
    ID("id"),
    INTEGER("integer"),
    LAYOUT("layout"),
    MENU("menu"),
    MIPMAP("mipmap"),
    RAW("raw"),
    STRING("string"),
    STYLE("style"),
    XML("xml");
}