package com.test.base.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding


/**
 * author : huazi
 * time   : 2020/3/13
 * desc   : activity基类
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var activity : AppCompatActivity
    protected lateinit var context: Context
    private lateinit var _binding: VB
    protected val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTranslucent(this)
        _binding = getViewBinding()
        setContentView(_binding.root)
        activity = this
        context = this
        setNavigationBarColor(activity)
        initData()
        initView()
    }

    /** binding layout*/
    protected abstract fun getViewBinding(): VB
    /** 初始化view*/
    abstract fun initView()
    /** 接收数据*/
    protected open fun initData() {

    }

    override fun getResources(): Resources {
        // 禁止app字体大小跟随系统字体大小调节
        val resources = super.getResources()
        if (resources != null && resources.configuration.fontScale != 1.0f) {
            val configuration = resources.configuration
            configuration.fontScale = 1.0f
            //resources.updateConfiguration(configuration, resources.displayMetrics)
            createConfigurationContext(configuration)
        }
        return resources
    }

    /** 处理状态栏*/
    private fun setTranslucent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun onPause() {
        super.onPause()
        //currentFocus?.clearFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 设置虚拟按键背景
     */
    @SuppressLint("NewApi")
    private fun setNavigationBarColor(context: Context?) {
        //判断系统5.0以上, 并且底部有虚拟按键
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        val isSys5 = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP
        if (!hasMenuKey and !hasBackKey and isSys5) {
            // 虚拟导航键
            window.navigationBarColor = Color.WHITE
        }
    }

    /**
     * 添加fragment
     */
    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    protected fun FragmentActivity.addFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    protected fun FragmentActivity.replaceFragment(frameId: Int, fragment: Fragment) {
        supportFragmentManager.inTransaction { replace(frameId, fragment) }
    }
}