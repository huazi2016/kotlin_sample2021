package com.test.base.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.base.db.bo.TestDbBo

/**
 * author : huazi
 * time   : 2020/01/01
 * desc   :测试dao
 */
@Dao
interface TestDao {

    /** 获取所有的用户数据*/
    @Query("SELECT * FROM test")
    suspend fun queryTest(): TestDbBo?

    /** 表中已有相同数据, 直接忽略*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(vararg user: TestDbBo)
}