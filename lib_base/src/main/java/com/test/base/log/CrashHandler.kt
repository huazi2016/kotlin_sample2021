package com.test.base.log

import android.os.Process
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * author : huazi
 * time   : 2021/1/28
 * desc   : 崩溃日志拦截
 */
object CrashHandler : Thread.UncaughtExceptionHandler {

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    fun initCrashLog() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, ex: Throwable) {
        GlobalScope.launch(Dispatchers.IO) {
            if (ex is OutOfMemoryError) {
                LogUtil.setLog("CrashHandler--" + getOtherThreadsInfo(t))
            }
            val elements: Array<StackTraceElement> = ex.stackTrace
            val reason: StringBuilder = StringBuilder(ex.toString())
            if (elements.isNotEmpty()) {
                for (element in elements) {
                    reason.append("\n")
                    reason.append(element.toString())
                }
            }
            LogUtil.setLog("CrashHandler--errMsg==$reason")
        }
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就自己结束自己
        if (defaultHandler != null) {
            defaultHandler!!.uncaughtException(t, ex)
        } else {
            Process.killProcess(Process.myPid())
        }
    }

    /**
     * 内存泄漏, 打印堆栈信息
     * @param crashedThread 线程
     */
    private fun getOtherThreadsInfo(crashedThread: Thread?): String {
        var thdDumped = 0
        val sb = StringBuilder()
        val map = Thread.getAllStackTraces()
        for ((thd, stacktrace) in map) {
            //skip the crashed thread
            val name = if (crashedThread == null) "null" else crashedThread.name
            if (thd.name == name) continue
            sb.append("------------" + "\n")
            sb.append("tid: ").append(thd.id).append(", name: ").append(thd.name).append(" <<<\n")
            sb.append("\n")
            sb.append("java stacktrace:\n")
            for (element in stacktrace) {
                sb.append("    at ").append(element.toString()).append("\n")
            }
            sb.append("\n")
            thdDumped++
        }
        if (map.size > 1) {
            if (thdDumped == 0) {
                sb.append("------------" + "\n")
            }
            sb.append("total JVM threads (exclude the crashed thread): ").append(map.size - 1)
                .append("\n")
            sb.append("dumped JVM threads:").append(thdDumped).append("\n")
            sb.append("------------" + "\n")
        }
        return sb.toString()
    }

}