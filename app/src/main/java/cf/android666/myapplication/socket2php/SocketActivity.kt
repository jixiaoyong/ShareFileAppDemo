package cf.android666.myapplication.socket2php

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cf.android666.myapplication.R
import cf.android666.myapplication.R.id.btn1
import cf.android666.myapplication.R.id.port
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_socket.*
import java.io.IOException
import java.io.InputStream
import java.net.Socket
import java.net.UnknownHostException

/**
 * Created by jixiaoyong on 2018/8/29.
 * email:jixiaoyong1995@gmail.com
 */
class SocketActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket)

        btn1.setOnClickListener {
            requestNet(port.text.toString().toInt())
        }

    }

    private fun requestNet(port:Int) {

        Thread {
            try {

                Logger.d("准备连接");
                var socket = Socket("192.168.2.171", port);
                Logger.d("连接上了");

//            var intent =  Intent();
//            intent.setClass(SocketTest.this, ConnectActivity.class)
//            startActivity(intent);
                var inputStream = socket.getInputStream();
                var buffer = ByteArray(1024 * 4)
                var temp = 0;
                var res = ""
                //从inputstream中读取客户端所发送的数据
                Logger.d("接收到服务器的信息是：");
                while (true) {
                    temp = inputStream.read(buffer)
                    if (temp == -1) {
                        break
                    }
                    Logger.d(String(buffer, 0, temp))
                    res += String(buffer, 0, temp);
                }


                runOnUiThread{
                    text.text = res
                }

                Logger.d("已经结束接收信息……");

                socket.close();
                inputStream.close();

            } catch (e: UnknownHostException) {
                e.printStackTrace();
            } catch (e: IOException) {
                e.printStackTrace();
            }

        }.start()
    }
}