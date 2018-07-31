package cf.android666.myapplication.timeline

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cf.android666.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_1.setNeedTop(false)
        text_6.setNeedBottom(false)



    }
}
