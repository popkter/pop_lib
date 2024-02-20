package com.popkter.poplib.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.popkter.poplib.utils.LibUtils.application
import com.popkter.poplib.utils.LibUtils.displayMetrics
import java.lang.reflect.ParameterizedType
import java.util.concurrent.atomic.AtomicInteger

object LibUtils {
    private const val TAG = "LibUtils"
    private lateinit var _application: Application
    internal val _activityCache = mutableMapOf<Int, Activity>()
    internal val _activityCount = AtomicInteger(0)

    val displayMetrics = DisplayMetrics()

    val application: Application
        get() = _application

    fun init(context: Application) {
        _application = context

        //监听Activity的生命周期
        _application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                _activityCache[_activityCount.incrementAndGet()] = activity
                Log.e(
                    TAG,
                    "onActivityCreated ${activity.javaClass.simpleName} ${_activityCount.get()} ${_activityCache.size}"
                )

            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                //设置深色模式
                if (isDarkMode()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(
                activity: Activity,
                outState: Bundle
            ) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                _activityCache[_activityCount.get()]?.also {
                    if (it == activity) {
                        _activityCache.remove(_activityCount.get())
                        _activityCount.decrementAndGet()
                    }
                }
            }
        })

        //测量屏幕尺寸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            application.getSystemService(Context.WINDOW_SERVICE)?.let {
                (it as WindowManager).currentWindowMetrics.bounds.also { bounds ->
                    displayMetrics.widthPixels = bounds.width()
                    displayMetrics.heightPixels = bounds.height()
                }
            }
        } else {
            (application.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
                displayMetrics
            )
        }
    }
}

internal val topActivity
    get() = LibUtils._activityCache[LibUtils._activityCount.get()]!!

/**
 * 屏幕尺寸
 */
internal val screenSize
    get() = Rect(0, 0, screenWidth, screenHeight)

/**
 * 屏幕宽度
 */
internal val screenWidth
    get() = displayMetrics.widthPixels

/**
 * 屏幕高度
 */
internal val screenHeight
    get() = displayMetrics.heightPixels


/**
 * Check whether the suspension window permission is enabled
 */
fun checkSuspensionWindowPermission(context: Activity, block: () -> Unit) {
    if (Settings.canDrawOverlays(context)) {
        block()
    } else {
        Toast.makeText(context, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show()
        context.startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            data = Uri.parse("package:${context.packageName}")
        }, 1001)
    }
}

/**
 * DP 转 PX
 */
fun Float.dp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * 测量View
 */
fun View.doMeasure() {
    val widthSpec = View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.AT_MOST)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(screenHeight, View.MeasureSpec.AT_MOST)
    this.measure(widthSpec, heightSpec)
}


/**
 * 是否是深色主题模式
 */
fun isDarkMode(): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val nightModeFlags = AppCompatDelegate.getDefaultNightMode()
        nightModeFlags == AppCompatDelegate.MODE_NIGHT_YES
    } else {
        val nightModeFlags =
            application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}

/**
 * 获取Activity的ViewBinding
 * 作者：一个被摄影耽误的程序猿
 * 链接：https://juejin.cn/post/6957608813809795108/
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Any.getActivityBinding(inflater: LayoutInflater, position: Int = 0): VB {
    val vbClass =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
    val inflate = vbClass[position].getDeclaredMethod("inflate", LayoutInflater::class.java)
    return inflate.invoke(null, inflater) as VB
}

/**
 * 获取ViewBinding
 * 作者：一个被摄影耽误的程序猿
 * 链接：https://juejin.cn/post/6957608813809795108/
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Any.getBinding(
    inflater: LayoutInflater,
    container: ViewGroup?,
    position: Int = 0
): VB {
    val vbClass =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
    val inflate = vbClass[position].getDeclaredMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    return inflate.invoke(null, inflater, container, false) as VB
}

/**
 * 获取ViewModel
 * 作者：Amin
 * 链接：https://stackoverflow.com/questions/67778043/how-to-use-generics-for-viewmodel-in-kotlin
 * 来源：StackOverFlow
 */
@Suppress("UNCHECKED_CAST")
fun <VM : ViewModel> ViewModelStoreOwner.getViewModel(): VM {
    val parameterizedType = javaClass.genericSuperclass as? ParameterizedType
    val vmClass = parameterizedType?.actualTypeArguments?.getOrNull(0) as? Class<VM>?
    if (vmClass != null)
        return ViewModelProvider(this)[vmClass]
    else
        throw IllegalArgumentException("could not find VM class for $this")
}

