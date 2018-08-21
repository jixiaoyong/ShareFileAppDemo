package cf.android666.myapplication.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import cf.android666.myapplication.R
import kotlinx.android.synthetic.main.activity_database.*

/**
 * Created by jixiaoyong on 2018/8/21.
 * email:jixiaoyong1995@gmail.com
 */
public class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainActivity"
    private lateinit var dataBaseHelper: DataBaseHelper

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        dataBaseHelper = DataBaseHelper(this, "data_base")
        initView()

    }

    private fun initView() {
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btn1 -> {
                var database = dataBaseHelper.writableDatabase
                Log.d(TAG, database.path)
                database.close()
            }
            R.id.btn2 -> {
                var database = dataBaseHelper.writableDatabase
                var contentValues = ContentValues()
                contentValues.put("id", id++)
                contentValues.put("name","name is $id")
                database.insert("user", null, contentValues)
                database.close()
            }
            R.id.btn3 -> {
                var database = dataBaseHelper.writableDatabase
                database.delete("user", "id=?", arrayOf("2"))
                database.close()

            }
            R.id.btn4 -> {
                var database = dataBaseHelper.writableDatabase
                var contentValues = ContentValues()
                contentValues.put("id", 233)
                database.update("user",  contentValues,"id=?", arrayOf("1"))
                database.close()

            }
            R.id.btn5 -> {
                var database = dataBaseHelper.readableDatabase
                var data = database.query("user", arrayOf("id","name"),
                        "id=?", arrayOf("2"), null, null, null)
                Log.d(TAG, "data size is ${data.count}")

                while (data.moveToNext()) {
                    var id = data.getString(data.getColumnIndex("id"))
                    var name = data.getString(data.getColumnIndex("name"))
                    Log.d(TAG,"id is $id,name is $name")
                }
                database.close()

            }
        }


    }
}