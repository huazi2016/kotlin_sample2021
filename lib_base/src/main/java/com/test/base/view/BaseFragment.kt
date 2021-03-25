package com.test.base.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * author : huazi
 * time   : 2020/9/3
 * desc   : fragment基类
 */
abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    /** 当前界面 activity 对象*/
    protected lateinit var activity: Activity
    private lateinit var _binding: VB
    protected val binding get() = _binding

    /** 根布局对象 */
    protected var rootView: View? = null

    /** 标记 - 第一次加载 */
    protected var isFirstLoad = true
        private set(value) {
            field = value
        }

    private var isLoaded = false

    override fun onPause() {
        super.onPause()
        isFirstLoad = false
        //停止加载
        stopLoad()
    }

    override fun onResume() {
        super.onResume()
        //懒加载
        lazyLoad()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = getBinding(inflater, container)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 若fragment不需使用findViewById, initView()必须放到这里
        initView()
        // 初始化观察者
        initObserve()
    }

    /** 设置布局layout*/
    //abstract fun setLayoutId(): Int
    abstract fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): VB

    /**
     * 初始化布局
     */
    abstract fun initView()

    /**
     * 初始化观察者
     */
    protected open fun initObserve() {

    }

    /**
     * 懒加载
     */
    protected open fun lazyLoad() {

    }

    /**
     * 停止加载
     */
    protected open fun stopLoad() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    /**
     * 定时刷新方法
     */
    fun timerToRefresh(duration: Long = REFRESH_DURATION, block: () -> Unit) {
        lifecycleScope.launch {
            while (this.isActive) {
                delay(duration)
                if (this@BaseFragment.isResumed && this@BaseFragment.withRootFragment { !isHidden }) {
                    block()
                }
            }
        }
    }

    /**
     * 在根fragment中执行,并返回
     */
    private fun <T> withRootFragment(block: Fragment.() -> T): T {
        var f: Fragment = this
        while (f.parentFragment != null) {
            f.parentFragment?.apply {
                f = this
            }
        }
        return f.block()
    }

    /**
     * 添加fragment
     */
    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    protected fun FragmentActivity.addFragment(frameId: Int, fragment: Fragment) {
        childFragmentManager.inTransaction { add(frameId, fragment) }
    }

    protected fun FragmentActivity.replaceFragment(frameId: Int, fragment: Fragment) {
        childFragmentManager.inTransaction { replace(frameId, fragment) }
    }

    companion object {
        private const val REFRESH_DURATION = 15000L
    }
}