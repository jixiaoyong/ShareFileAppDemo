package cf.android666.myapplication.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by jixiaoyong on 2018/8/21.
 * email:jixiaoyong1995@gmail.com
 */
public class DataBaseHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                            version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    constructor(context: MainActivity, name: String) : this(context, name, null, VERSION)

    companion object {
        const val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val cmdStr = "create table user(id int primary key,name varchar(200))"
        db?.execSQL(cmdStr)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("DATABASE", "数据库版本: $oldVersion -> $newVersion")
    }

}