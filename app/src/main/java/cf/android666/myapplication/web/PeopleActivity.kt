package cf.android666.myapplication.web

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import cf.android666.myapplication.R
import cf.android666.myapplication.web.Utils.getStringFromInputStream
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_people.*
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

/**
 * Created by jixiaoyong on 2018/8/26.
 * email:jixiaoyong1995@gmail.com
 */
class PeopleActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

        mContext = this

        if (intent != null) {
            name.setText(intent.getStringExtra("name"))
            age.setText(intent.getStringExtra("age"))
            email.setText(intent.getStringExtra("email"))
            time.setText(intent.getStringExtra("create_time"))
        }

        add.setOnClickListener(this)
        update.setOnClickListener(this)
        delete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add -> { updatePeople(2) }
            R.id.delete -> { updatePeople(3) }
            R.id.update -> { updatePeople(4) }
        }
    }

    private fun updatePeople(code:Int) {

        Thread {
            var urlStr = Utils.loadManifest(this, "ServerUrl")
            var url = URL(urlStr)
            var conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("request_key", "value")
            conn.readTimeout = 5000
            conn.connectTimeout = 30000
            conn.doOutput = true;
            conn.doInput = true;
            conn.setRequestProperty("Connection", "Keep-Alive")
            conn.setRequestProperty("Charset", "UTF-8")
            var output: OutputStream? = null
            conn.connect()
            try {
                if (true) {

                    output = conn.outputStream
                    var string = "request_code=$code&username=${name.text}&age=${age.text}" +
                            "&email=${email.text}&id=${intent.getIntExtra("id",-1)}"
                    output.write(string.toByteArray())
                    output.flush()

                    var input = conn.inputStream
                    var jsonStr = getStringFromInputStream(input)

                    Logger.d(jsonStr)

                }
            } catch (e: SocketTimeoutException) {
                runOnUiThread {
                    Toast.makeText(mContext, "数据请求超时，请稍后重试", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            } finally {
                output?.close()
                conn.disconnect()
            }
        }.start()
    }


}