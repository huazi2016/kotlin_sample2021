@file:Suppress("unused")
@file:JvmName("IntentTools")

package com.test.base.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

/**
 * 跳转拨号盘
 *
 * @param phoneNumber 手机号
 * @param context [Context] 对象
 */
@JvmOverloads
fun jumpToDial(phoneNumber: String? = null, context: Context = AppManager.getContext()) {
    context.startActivity(if (phoneNumber.isNullOrBlank()) {
        Intent(Intent.ACTION_CALL_BUTTON)
    } else {
        val uri = if (phoneNumber.startsWith("tel:")) {
            phoneNumber
        } else {
            "tel:$phoneNumber"
        }
        Intent(Intent.ACTION_DIAL, Uri.parse(uri))
    })
}

/**
 * 跳转通知设置界面
 *
 * @param context [Context] 对象
 */
@JvmOverloads
fun jumpToNotificationSetting(context: Context = AppManager.getContext()) {
    try {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // 直接跳转到应用通知设置的代码：
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                // 26 及以上
                localIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(localIntent)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                // 21 及以上
                localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                localIntent.putExtra("app_package", context.packageName)
                localIntent.putExtra("app_uid", context.applicationInfo.uid)
                context.startActivity(localIntent)
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT -> {
                // 19 及以上
                localIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                localIntent.addCategory(Intent.CATEGORY_DEFAULT)
                localIntent.data = Uri.parse("package:" + context.packageName)
                context.startActivity(localIntent)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 跳转应用详情界面
 *
 * @param context [Context] 对象
 */
@JvmOverloads
fun jumpToAppDetails(context: Context = AppManager.getContext()) {
    context.startActivity(Intent().apply {
        if (context !is Activity) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.parse("package:" + context.packageName)
    })
}

/**
 * 跳转手机浏览器
 *
 * @param url 网页地址
 * @param context [Context] 对象
 */
@JvmOverloads
fun jumpToBrowser(url: String, context: Context = AppManager.getContext()) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(browserIntent)
}

/**
 * 跳转开发者设置
 *
 * @param context [Context] 对象
 */
@JvmOverloads
fun jumpToDevelopment(context: Context = AppManager.getContext()) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).apply {
            if (context !is Activity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val componentName = ComponentName(
                    "com.android.settings",
                    "com.android.settings.DevelopmentSettings"
            )
            val intent = Intent().apply {
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            intent.component = componentName
            intent.action = "android.intent.action.View"
            context.startActivity(intent)
        } catch (e1: Exception) {
            try {
                // 部分小米手机采用这种方式跳转
                val intent = Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS").apply {
                    if (context !is Activity) {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
                context.startActivity(intent)
            } catch (e2: Exception) {
            }
        }
    }
}