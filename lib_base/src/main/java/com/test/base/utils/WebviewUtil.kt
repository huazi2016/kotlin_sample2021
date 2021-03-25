package com.test.base.utils

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * author : huazi
 * time   : 2020/10/31
 * desc   : webview工具类
 */
object WebviewUtil {

    /**
     * 初始化webview
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(webView: WebView) {
        val settings = webView.settings
        settings.apply {
            //设置支持Js,必须设置的,基本大多数网页都涉及js
            javaScriptEnabled = true
            //设置为true时表示支持使用js打开新的窗口
            //javaScriptCanOpenWindowsAutomatically = true
            //当网页需要保存数时据,设置下面属性
            domStorageEnabled = true
            //设置为使用webview推荐的窗口，主要是为了配合下一个属性
            useWideViewPort = true
            //开启H5地理位置
            //setGeolocationEnabled(true)
            //设置是否允许webview使用缩放的功能
            builtInZoomControls = false
            //提高网页渲染的优先级
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            //网页缓存机制
            cacheMode = WebSettings.LOAD_NO_CACHE
            pluginState = WebSettings.PluginState.ON
            //默认编码
            defaultTextEncodingName = "utf-8"
        }
        //设置是否显示水平滚动条
        webView.isHorizontalScrollBarEnabled = false
        //设置垂直滚动条是否有叠加样式
        webView.setVerticalScrollbarOverlay(true)
        //设置滚动条的样式
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY;
    }
}