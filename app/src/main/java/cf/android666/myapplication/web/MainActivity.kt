package cf.android666.myapplication.web

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import cf.android666.myapplication.R
import cf.android666.myapplication.R.id.recycler
import cf.android666.myapplication.qrcode.MainActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_net.*
import kotlinx.android.synthetic.main.item_net.view.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL

/**
 * Created by jixiaoyong on 2018/8/25.
 * email:jixiaoyong1995@gmail.com
 */
public class MainActivity : AppCompatActivity() {

    private lateinit var mContext:Context

    private var mData: Array<JsonClass.Arr> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net)
        mContext = this

        urlStr = Utils.loadManifest(this, "ServerUrl")

        initView()
        getData()


    }

    private var urlStr = ""

    private fun getData(){
        Thread(Runnable {
            var url = URL(urlStr)
            var conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.readTimeout = 5000
            conn.connectTimeout = 10000

            if (conn.responseCode == 200) {

                var input = conn.inputStream
                var jsonStr = getStringFromInputStream(input)
                Log.d("TAG",jsonStr)
                mData = Gson().fromJson(jsonStr, JsonClass::class.java).arr
                runOnUiThread(Runnable {
                    recycler.adapter.notifyDataSetChanged()
                })
            }

        }).start()

    }

    @Throws(IOException::class)
    private fun getStringFromInputStream(`is`: InputStream): String {
        val os = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = -1
        while (true) {
            len = `is`.read(buffer)
            if (len == -1) {
                break
            }
            os.write(buffer, 0, len)
        }
        `is`.close()
        val state = os.toString() //把字节流转字符串
        os.close()
        return state
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
                        "\nemail:${mData[position].email}" +
                        "\ncreate_time:${mData[position].create_time}"
            }

        }

        refresh_btn.setOnClickListener {
            if (!api_url.text.toString().isEmpty()) {
                urlStr = api_url.text.toString()
            }
            getData()
        }
        scan_btn.setOnClickListener {
            startActivityForResult(Intent(mContext, MainActivity::class.java),1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            api_url.setText(data?.getStringExtra("host"))

        }
    }

    class MViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
}