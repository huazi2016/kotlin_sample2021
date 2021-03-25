package com.test.base.utils

import com.test.base.const.SERVICE_TIME
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV

/**
 * author : huazi
 * time   : 2020/9/24
 * desc   : mmkv工具类
 */
object MmkvUtil {

    /**
     * 获取服务器时间
     */
    fun getServicesTime(): Long {
        //获取不到服务器时间, 再取本地时间
        var servicesTime = MMKV.defaultMMKV().decodeLong(SERVICE_TIME, 0)
        if (servicesTime <= 0) {
            servicesTime = System.currentTimeMillis()
        }
        return servicesTime
    }

    /**
     * 返回对应格式化的日期
     * @return 2020-10-19
     */
    fun getServicesTimeFormat(): String {
        getServicesTime().apply {
            return TimeTiUtil.formatTimeMilli(this, TIME_STYLE16)
        }
    }

    /**
     * 判断是否同一天
     */
    fun isSameDay(): Boolean {
        val servicesTime = MMKV.defaultMMKV().decodeLong(SERVICE_TIME, 0)
        val currentTimeMillis = System.currentTimeMillis()
        if (servicesTime > 0 && currentTimeMillis > 0) {
            val servicesTimeFormat =
                TimeTiUtil.formatTimeMilli(servicesTime, TIME_STYLE16)
            val systemTime = TimeTiUtil.formatTimeMilli(currentTimeMillis, TIME_STYLE16)
            Logger.d("isSameDay--servicesTime:$servicesTimeFormat--systemTime:$systemTime")
            return servicesTimeFormat == systemTime
        }
        return false
    }

//    /**
//     * 获取首页底部bar数据
//     */
//    fun getMainTabData(code: String): TabInfoBo {
//        var isEmpty = true
//        var infoBo: TabInfoBo? = null
//        val json = MMKV.defaultMMKV().decodeString(code, "")
//        if (!TextUtils.isEmpty(json)) {
//            infoBo = Gson().fromJson(json, TabInfoBo::class.java)
//            if (infoBo != null && infoBo.tbl.isNotNullAndEmpty()) {
//                isEmpty = false
//            }
//        }
//        //获取不到网络数据, 再获取assets--json数据
//        if (isEmpty) {
//            val jsonLocal = MMKV.defaultMMKV().decodeString(CACHE_MAIN_TAB, "")
//            if (!TextUtils.isEmpty(jsonLocal)) {
//                infoBo = Gson().fromJson(jsonLocal, TabInfoBo::class.java)
//            }
//        }
//        return infoBo!!
//    }
//
//    /**
//     * 获取首页顶部bar数据
//     */
//    fun getHomeTopTabData(code: String, cacheCode: String): TabInfoBo {
//        var isEmpty = true
//        var infoBo: TabInfoBo? = null
//        val json = MMKV.defaultMMKV().decodeString(code, "")
//        if (!TextUtils.isEmpty(json)) {
//            infoBo = Gson().fromJson(json, TabInfoBo::class.java)
//            if (infoBo != null && infoBo.tbl.isNotNullAndEmpty()) {
//                isEmpty = false
//            }
//        }
//        //获取不到网络数据, 再获取assets--json数据
//        if (isEmpty) {
//            val jsonLocal = MMKV.defaultMMKV().decodeString(cacheCode, "")
//            if (!TextUtils.isEmpty(jsonLocal)) {
//                infoBo = Gson().fromJson(jsonLocal, TabInfoBo::class.java)
//            }
//        }
//        return infoBo!!
//    }
//
//    /**
//     * 获取首页-比赛状态bar数据
//     */
//    fun getGameStateTabData(id: String, code: String, cacheCode: String): List<SecondTabBo> {
//        var isEmpty = true
//        var tabList = arrayListOf<SecondTabBo>()
//        val infoBo = getHomeTopTabData(code, cacheCode)
//        if (infoBo.tbl.isNotNullAndEmpty()) {
//            for (firstTabBo in infoBo.tbl!!) {
//                if (firstTabBo.id == id) {
//                    val secondTabList = firstTabBo.tblitem
//                    if (secondTabList.isNotNullAndEmpty()) {
//                        tabList = secondTabList!!
//                        isEmpty = false
//                    }
//                }
//            }
//        }
//        //获取不到网络数据, 再获取assets--json数据
//        if (isEmpty) {
//            tabList = getLocalSecondTabInfo(cacheCode, id, tabList)
//        }
//        return tabList
//    }
//
//    /**
//     * 获取本地assets--json二级tab数据
//     */
//    private fun getLocalSecondTabInfo(cacheCode: String, id: String, tabList: ArrayList<SecondTabBo>)
//            : ArrayList<SecondTabBo> {
//        var tabList1 = tabList
//        val jsonLocal = MMKV.defaultMMKV().decodeString(cacheCode, "")
//        if (!TextUtils.isEmpty(jsonLocal)) {
//            val infoBoLocal = Gson().fromJson(jsonLocal, TabInfoBo::class.java)
//            if (infoBoLocal != null && infoBoLocal.tbl.isNotNullAndEmpty()) {
//                val tblList = infoBoLocal.tbl!!
//                for (firstTabBo in tblList) {
//                    if (firstTabBo.id == id) {
//                        val secondTabList = firstTabBo.tblitem
//                        if (secondTabList.isNotNullAndEmpty()) {
//                            tabList1 = secondTabList!!
//                        }
//                    }
//                }
//            }
//        }
//        return tabList1
//    }
//
//    /**
//     * 获取比赛详情bar数据
//     */
//    fun getMatchDetailTabData(gameId: String): ArrayList<FirstTabBo> {
//        var isEmpty = true
//        var tblLocalList = arrayListOf<FirstTabBo>()
//        val json = MMKV.defaultMMKV().decodeString(MATCH_DETAIL_TAB + "_" + gameId, "")
//        if (!TextUtils.isEmpty(json)) {
//            val infoBo = Gson().fromJson(json, TabInfoAllBo::class.java)
//            if (infoBo != null && infoBo.tbls.isNotNullAndEmpty()) {
//                val tblList = infoBo.tbls!!
//                if (tblList.isNotNullAndEmpty()) {
//                    tblLocalList = tblList[0].tbl!!
//                    isEmpty = false
//                }
//            }
//        }
//        if (isEmpty) {
//            val tab01 = FirstTabBo()
//            tab01.idx = 0
//            tab01.id = "END6"
//            tab01.name = "数据"
//            tblLocalList.add(tab01)
//            //val tab02 = FirstTabBo()
//            //tab02.idx = 1
//            //tab02.id = "END7"
//            //tab02.name = "聊天"
//            //tblLocalList.add(tab02)
//            if (isLOL(gameId) || isDOTA(gameId)) {
//                val tab03 = FirstTabBo()
//                tab03.idx = 2
//                tab03.id = "END8"
//                tab03.name = "阵容"
//                tblLocalList.add(tab03)
//            }
//            val tab04 = FirstTabBo()
//            tab04.idx = 3
//            tab04.id = "END9"
//            tab04.name = "选手"
//            tblLocalList.add(tab04)
//            val tab05 = FirstTabBo()
//            tab05.idx = 4
//            tab05.id = "END10"
//            tab05.name = "分析"
//            tblLocalList.add(tab05)
//            if (isCSGO(gameId)) {
//                val tab06 = FirstTabBo()
//                tab06.idx = 5
//                tab06.id = "END11"
//                tab06.name = "地图"
//                tblLocalList.add(tab06)
//            }
//        }
//        return tblLocalList
//    }
//    /**
//     * 获取tab
//     * 首先取网络,如果网络请求成功,保存数据到本地并;如果失败,取本地数据展示
//     */
//    fun getTabs(code: String): TabInfoBo{
//        var isEmpty = true
//        var infoBo: TabInfoBo? = null
//        val json = MMKV.defaultMMKV().decodeString(code, "")
//        if (!TextUtils.isEmpty(json)) {
//            infoBo = Gson().fromJson(json, TabInfoBo::class.java)
//            if (infoBo != null && infoBo.tbl.isNotNullAndEmpty()) {
//                isEmpty = false
//            }
//        }
//        return infoBo!!
//    }
}