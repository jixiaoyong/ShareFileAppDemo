package cf.android666.myapplication.greendao

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cf.android666.myapplication.MyApplication
import cf.android666.myapplication.R
import kotlinx.android.synthetic.main.activity_dao.*
import kotlinx.android.synthetic.main.activity_dao.view.*
import kotlinx.android.synthetic.main.item_one_text.view.*
import org.greenrobot.greendao.database.Database

/**
 * Created by jixiaoyong on 2018/8/29.
 * email:jixiaoyong1995@gmail.com
 */
class DaoActivity : AppCompatActivity() {

    private  var strings=  ArrayList<String>()

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dao)
        mContext = this

        var userDao = (application as MyApplication).getmDaoSession().userDao
        userDao.queryBuilder().list().map {
            strings.add("id:${it.id}\nname: ${it.name}\nmsg:${it.msg}")
        }

        initView()

        add.setOnClickListener {

            if ("".equals(name.text.toString()) || "".equals(msg.text.toString())) {
                return@setOnClickListener
            }
            userDao.insert(User(strings.size + 1L, name.text.toString(), msg.text.toString()))
            strings.clear()
            var userDao = (application as MyApplication).getmDaoSession().userDao
            userDao.queryBuilder().list().map {
                strings.add("id:${it.id}\nname: ${it.name}\nmsg:${it.msg}")
            }
            recycler.adapter.notifyDataSetChanged()
        }

    }

    private fun initView() {

        recycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = object : RecyclerView.Adapter<UserViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                return UserViewHolder(layoutInflater.inflate(R.layout.item_one_text, parent, false))
            }

            override fun getItemCount(): Int {
                return strings.size
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
                holder.itemView.text.text = strings[position]
            }
        }
    }

    inner class UserViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
}