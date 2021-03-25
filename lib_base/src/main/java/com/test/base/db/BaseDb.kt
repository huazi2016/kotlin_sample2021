package com.test.base.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.test.base.db.bo.TestDbBo
import com.test.base.db.bo.UserDbBo
import com.test.base.db.dao.TestDao
import com.test.base.db.dao.UserDao
import com.test.base.utils.AppManager
import com.orhanobut.logger.Logger

/** 数据库名称*/
private const val DB_NAME = "lkr_db"

/**
 * author : huazi
 * time   : 2020/10/13
 * desc   :
 * @param version 数据库版本号, 升级需自增
 * @param exportSchema 数据库迁移, 设置为false以避免生成警告
 */
@Database(entities = [UserDbBo::class, TestDbBo::class], version = 2, exportSchema = false)
abstract class BaseDb : RoomDatabase() {
    //用户Dao
    abstract fun getUserDao(): UserDao
    abstract fun getTestDao(): TestDao
}

/**
 * 版本号从1-2, 2-3, 再复写, 以此类型, addMigrations可接收多个参数
 *
 * //database.execSQL("CREATE TABLE IF NOT EXISTS `TiLoginBo` (`indexKey` INTEGER NOT NULL DEFAULT 0, `name` TEXT, " +
 * //        "`age` INTEGER NOT NULL DEFAULT 0, `nick` TEXT)")
 *  //database.execSQL("ALTER TABLE user ADD COLUMN `test_str` TEXT")
 *  //database.execSQL("DROP TABLE user")
 *  // 创建新表
 *  //database.execSQL("CREATE TABLE user (id TEXT NOT NULL, name TEXT, head TEXT, token TEXT, PRIMARY KEY(id))");
 *  // 拷贝数据(将数据从users表复制到临时表)
 *  //database.execSQL("INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
 *  // 删除老的表(删了users表)
 *  //database.execSQL("DROP TABLE users");
 *  // 改名(将临时表重命名为users)
 *  //database.execSQL("ALTER TABLE users_new RENAME TO users");
 *
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //版本升级, 修改表结构, 需要处理
        Logger.d("huazi_db=" + "migrate_success")
        //新增表字段, NOT NULL DEFAULT 0 会抛异常
        //database.execSQL("ALTER TABLE user ADD COLUMN test_str INTEGER NOT NULL DEFAULT 0")
        database.execSQL("CREATE TABLE test (id  INTEGER NOT NULL DEFAULT 0, user_name TEXT NOT NULL DEFAULT '', " +
                "user_age INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(id))")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE user ADD COLUMN long_time TEXT NOT NULL DEFAULT 'sky'")
        //boolean值用INTEGER类型
        database.execSQL("ALTER TABLE user ADD COLUMN is_select INTEGER NOT NULL DEFAULT 1")
    }
}

/** 初始化room*/
val room = Room.databaseBuilder(AppManager.getContext(), BaseDb::class.java, DB_NAME)
    //允许在主线程执行SQL语句
    .allowMainThreadQueries()
    //检测version是否发生改变, 会删除数据库并重建
    //.fallbackToDestructiveMigration()
    //升级回调, 自动加载migration
    .addMigrations(MIGRATION_1_2)
    .build()

/** 对外暴露dao*/
val userDao= room.getUserDao()
val testDao= room.getTestDao()