package com.test.huazi.activity

import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV
import com.test.base.koin.netModule
import com.test.base.utils.AppManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * author : huazi
 * time   : 2021/3/25
 * desc   :
 */
class TestApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initLog()
        initKoin()
        //mmkv, 替代sp
        MMKV.initialize(applicationContext)
        //初始化appmanager
        AppManager.register(this)
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@TestApp)
            koin.loadModules(listOf(netModule))
        }
    }

    /**
     * init Logger日志
     */
    private fun initLog() {
        //若要写到文件(/storage/emulated/0/logger)，同时也要在控制台中看到，需要同时添加两个适配器
        val strategy = PrettyFormatStrategy.newBuilder()
            //是否显示线程信息。 默认值为true
            .showThreadInfo(false)
            //要显示的方法行数。 默认2
            .methodCount(2)
            //设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
            .methodOffset(0)
            //更改要打印的日志策略
            //.logStrategy(customLog)
            //每个日志的全局标记, Logger.t("lhx").d(diskPath) 局部标签定义
            .tag("LkrLog")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(strategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                //测试或开发环境才开放
                return BuildConfig.DEBUG
            }
        })
    }
}