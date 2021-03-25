package com.test.base.db.bo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable


/**
 * author : baoning
 * desc   : 用户相关Bo -- 用户数据对象
 */
@Entity(tableName = "user")
class UserDbBo : Serializable {

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var userId: String = "" //用户id

    @ColumnInfo(name = "nick_name")
    var nickName: String = "" //用户昵称

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String = "" //用户头像

    @ColumnInfo(name = "token")
    var token: String = "" //用户token，接口使用

    @ColumnInfo(name = "mobile")
    var mobile: String = "" //用户手机号

    /** 图片id*/
    @Ignore
    var avatarId: String = ""

    /** 图片名称*/
    @Ignore
    var avatarName: String = ""

    /** 登录时间*/
    @Ignore
    //@ColumnInfo(name = "long_time")
    var loginTime: String = ""

    @Ignore
    //@ColumnInfo(name = "test_str")
    var testStr: Int = 0

    @Ignore
    //@ColumnInfo(name = "is_select")
    var isSelect: Boolean = false

    @ColumnInfo(name = "flag")
    var flag: Int = 0 //0：老用户，1：新用户


    override fun toString(): String {
        return "UserBo(user_id='$userId', nick_name='$nickName', " +
                "avatar_url='$avatarUrl', token='$token', mobile='$mobile', " +
                "avatar_id='$avatarId', avatar_name='$avatarName', " +
                "login_time='$loginTime', isSelect=$isSelect, flag=$flag, testStr=$testStr)"
    }
}