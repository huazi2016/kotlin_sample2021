package com.test.base.net

/**
 * 网络接口地址
 */
object NetUrlAddress {

    /** 后台-开发环境--切环境用 */
    const val URL_DEV = "http://18pinpin.cn"
    /** 后台-测试环境--切环境用  */
    const val URL_DEBUG = "http://18pinpin.cn"
    /** 后台-现网--切环境用 */
    const val URL_RELEASE = "http://18pinpin.cn"

    /** 服务器url  */
    @Suppress("ConstantConditionIf")
    //val BASE_URL = BuildConfig.BASE_URL

    /** 获取短信验证码列表 */
    const val GET_SMS_CODE = "/IFAppSMSGetCode"
    /** 校验验证码 */
    const val CHECK_SMS_CODE = "/IFAppSMSCheckCode"
    /** 联赛list*/
    const val LOL_LG_LIST = "/IFAppUITournamentList "// "/lol/match/list"
    /** 获取tab 公共接口*/
    const val COMM_TAB_DATA = "/IFGetModelGroupDataAll"
    /** 获取tab 比赛详情*/
    const val DETAIL_TAB_DATA = "/IFGetModelGroupData"
    /** 获取服务器时间 公共接口*/
    const val GET_SERVICE_TIME = "/IFGetTime"
    /** 获取LOL比赛列表*/
    const val MATCH_LIST = "/IFAppUIMatch"
    /** 获取LOL直播简讯*/
    const val LIVE_INFORMATION = "/IFAppUIDetailsTop"
    /** 获取LOL直播详情*/
    const val LIVE_DETAIL = "/IFAppUILiveVideo"
    /** 比赛详情-选手*/
    const val LOL_PLAYER_INFO = "/IFAppUIPlayerInfo"
    /** 比赛详情-阵容*/
    const val LOL_BATTLE_ARRAY = "/IFAppUIBattleArrayNM"
    /** 比赛详情-csgo地图*/
    const val URL_CSGO_MAP = "/IFAppUIMap"
    /** 战队列表*/
    const val URL_TEAM_LIST = "/IFAppUIBattleTeamList"
    /** csgo分析数据*/
    const val URL_CSGO_ANA_DATA="/IFAppUIAnalyse"
    /** 用户登录*/
    const val URL_LOGIN="/UserCenter/IFLogin"
    /** 发送验证码*/
    const val URL_SMSCODE="/IFAppSMSGetCode"
    /** 查询邀请好友，关于我们等信息*/
    const val URL_INVITE_INFO="/IFAppGameData"
    /** 修改头像和昵称*/
    const val URL_CHANGE_USER_INFO="/UserCenter/IFAppModifyUserInfo"
    /** 获取头像集合*/
    const val URL_GET_HEAD_LIST="/UserCenter/IFGetInfo"
    /** 获取用户信息*/
    const val URL_GET_USER_INFO="/UserCenter/IFAppGetUserInfo"
    /** 退出登录*/
    const val URL_LOGOUT="/UserCenter/IFLogout"
    /** 用户注销*/
    const val URL_CANCEL_ACCOUNT="/UserCenter/IFUnRegisterUser"
    /** 设置用户密码*/
    const val URL_SET_PWD="/UserCenter/IFAppUserCenterModifyPass"
    /** 忘记密码*/
    const val URL_FORGET_SET_PWD="/UserCenter/IFAppUserCenterForgetPass"
    /** 修改手机号*/
    const val URL_CHANGE_PHONE="/UserCenter/IFAppUserCenterModifyPhone"
    /** 用户关注*/
    const val URL_USER_FOCUS = "/UserCenter/IFAppUserCenterSetFocus"
    /** 用户关注列表*/
    const val URL_FOCUS_LIST = "/UserCenter/IFAppUserCenterGetFocus"
    /** 战队荣耀*/
    const val BDD_HONOR = "/IFAppUI_BTDD_Honor"
    /** 战队详情-顶部 战队简讯*/
    const val BTD_NL = "/IFAppUI_BTD_NL"
    /** 战队-资料*/
    const val BTD_RESOURCE = "/IFAppUI_BTD_Resource"
    /** 战队-数据*/
    const val BTD_DATA = "/IFAppUI_BTD_Data"
    /** 战队详情-数据页面-成员及下拉列表*/
    const val BTD_DATA_MOL = "/IFAppUI_BTD_Data_MOL"
    /** 战队-比赛*/
    const val BTD_MATCH = "/IFAppUI_BTD_Match3"
    /** 战队队员详情- 队员简介及下拉列表*/
    const val PEO_DETAIL_TOP = "/IFAppUI_BTDD_PD_NL"
    /** 战队队员详情- 数据及比赛列表*/
    const val PEO_DETAIL_INFO = "/IFAppUI_BTDD_PD"
    /** 联赛详情-顶部*/
    const val TLD_NL = "/IFAppUI_TLD_NL"
    /** 联赛详情-战队*/
    const val TLD_BT = "/IFAppUI_TLD_BT"
    /** 联赛详情-比赛*/
    const val TLD_Match="/IFAppUI_TLD_Match"
    /** 联赛详情-数据榜*/
    const val TLD_RankList="/IFAppUI_TLD_RankList"
}
