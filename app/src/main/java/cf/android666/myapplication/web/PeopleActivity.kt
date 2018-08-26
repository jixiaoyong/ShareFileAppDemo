package cf.android666.myapplication.web

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cf.android666.myapplication.R
import kotlinx.android.synthetic.main.activity_people.*

/**
 * Created by jixiaoyong on 2018/8/26.
 * email:jixiaoyong1995@gmail.com
 */
class PeopleActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

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
            R.id.add -> {

            }
        }
    }
}