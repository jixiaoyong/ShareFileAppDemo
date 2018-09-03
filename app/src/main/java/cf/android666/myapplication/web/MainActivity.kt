package cf.android666.myapplication.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cf.android666.myapplication.R
import cf.android666.myapplication.lanp2p.RequestPermissionUtil
import cf.android666.myapplication.qrcode.MainActivity
import cf.android666.myapplication.web.Utils.getStringFromInputStream
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_net.*
import kotlinx.android.synthetic.main.item_net.view.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

/**
 * Created by jixiaoyong on 2018/8/25.
 * email:jixiaoyong1995@gmail.com
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mContext: Context

    private var mData: Array<JsonClass.Arr> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net)
        mContext = this

        urlStr = Utils.loadManifest(this, "ServerUrl") + "?request_code=1"

        initView()
        getData()

    }

    private var urlStr = ""

    private fun getData() {
        Thread(Runnable {
            var url = URL(urlStr)
            Log.d("TAG", "url is $urlStr")
            var conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.readTimeout = 5000
            conn.connectTimeout = 30000

            try {
                if (conn.responseCode == 200) {

                    var input = conn.inputStream
                    var jsonStr = getStringFromInputStream(input)
                    mData = Gson().fromJson(jsonStr, JsonClass::class.java).arr
                    runOnUiThread(Runnable {
                        recycler.adapter.notifyDataSetChanged()
                    })
                }
            } catch (e: SocketTimeoutException) {
                runOnUiThread {
                    Toast.makeText(mContext, "数据请求超时，请稍后重试", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }

        }).start()

    }


    private fun initView() {

        recycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = object : RecyclerView.Adapter<MViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
                return MViewHolder(layoutInflater.inflate(R.layout.item_net, parent, false))
            }

            override fun getItemCount(): Int {
                return mData.size
            }

            override fun onBindViewHolder(holder: MViewHolder, position: Int) {
                holder.itemView.text.text = "username :${mData[position].username}" +
                        "\nage:${mData[position].age}" +
                        "\nmsg:${mData[position].email}" +
                        "\ncreate_time:${mData[position].create_time}"
                holder.itemView.text.setOnClickListener {
                    var intent1 = Intent(mContext, PeopleActivity::class.java)
                    intent1.putExtra("id", mData[position].id)
                    intent1.putExtra("name", mData[position].username)
                    intent1.putExtra("age", mData[position].age.toString())
                    intent1.putExtra("email", mData[position].email)
                    intent1.putExtra("create_time", mData[position].create_time)
                    startActivity(intent1)
                }
            }

        }

        refresh_btn.setOnClickListener {
            if (!api_url.text.toString().isEmpty()) {
                urlStr = api_url.text.toString()
            }
            getData()
        }
        scan_btn.setOnClickListener {
            startActivityForResult(Intent(mContext, MainActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            api_url.setText(data?.getStringExtra("host"))
        }
    }

    class MViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}