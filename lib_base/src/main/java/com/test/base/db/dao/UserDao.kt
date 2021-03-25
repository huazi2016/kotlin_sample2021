package com.test.base.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.base.db.bo.UserDbBo
import com.test.base.db.userDao

/**
 * author : huazi
 * time   : 2020/01/01
 * desc   :用户信息dao
 */
@Dao
interface UserDao {

    /** 获取所有的用户数据*/
    @Query("SELECT * FROM user")
    suspend fun queryAllUser(): MutableList<UserDbBo>

    /** 获取唯一的用户数据*/
    @Query("SELECT * FROM user")
    fun queryUser(): UserDbBo?

    /** 表中已有相同数据, 直接忽略*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(vararg user: UserDbBo)

    /** 删除所有数据*/
    @Query("DELETE FROM user")
    suspend fun deleteAllUserInfo()

    /** 刷新Token*/
    @Query("UPDATE user SET token = :token WHERE user_id= :userId")
    suspend fun updateToken(userId: String, token: String)

    /** 刷新手机号*/
    @Query("UPDATE user SET mobile = :phone WHERE user_id= :userId")
    suspend fun updatePhone(userId: String, phone: String)

    /** 获取已登录的用户数据--查询失败, 暂时不用*/
    //@Query("SELECT user.token FROM user")
    //@Query("SELECT user.mobile FROM user")
    //suspend fun queryToken(): ArrayList<UserBo>

}

/** 获取登录状态*/
fun isLogin() : Boolean {
    var isLogin = false
    val userBo = userDao.queryUser()
    userBo?.apply {
        //userId不为空, 说明已经是登录状态
        isLogin = userBo.userId.isNotEmpty()
    }
    return isLogin
}

/** 获取token*/
fun getToken() : String {
    var token = ""
    val userBo = userDao.queryUser()
    userBo?.apply {
        token = userBo.token
    }
    return token
}

/** 获取登录手机号*/
fun getPhone() : String {
    var phone = ""
    val userBo = userDao.queryUser()
    userBo?.apply {
        phone = userBo.mobile
    }
    return phone
}

/** 获取用户ID*/
fun getUserId() : String {
    var userId = ""
    val userBo = userDao.queryUser()
    userBo?.apply {
        userId = userBo.userId
    }
    return userId
}