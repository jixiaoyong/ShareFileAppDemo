package cf.android666.myapplication.diy

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import cf.android666.myapplication.R
import kotlinx.android.synthetic.main.activity_diy.*
import java.util.*

/**
 * Created by jixiaoyong on 2018/9/3.
 * email:jixiaoyong1995@gmail.com
 */
class DiyActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diy)

        handler.sendMessageDelayed( handler.obtainMessage(1,PROGRESS++),100)

    }

    private var PROGRESS = 0f

    private val REFRESH_TEXT = 0
    private var handler = @SuppressLint("HandlerLeak")
    object :Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){
                REFRESH_TEXT->{
                    progress.progress = (msg.obj as Float)/100
                    sendMessageDelayed(obtainMessage(1),1000)
                }
                1->{
                        sendMessageDelayed(obtainMessage(REFRESH_TEXT,PROGRESS ++),1000)
                }
            }
        }
    }
}