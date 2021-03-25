package com.test.base.db.bo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable


/**
 * author : baoning
 * desc   : 测试相关Bo -- 测试数据对象
 */
@Entity(tableName = "test")
class TestDbBo : Serializable {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "user_name")
    var userName: String = "" //用户id

    @ColumnInfo(name = "user_age")
    var userAge: Int = 0 //用户昵称

    @Ignore
    var imgUrl: String = "" //用户昵称


    override fun toString(): String {
        return "TestDbBo(id=$id, userName='$userName', userAge=$userAge, imgUrl='$imgUrl')"
    }
}