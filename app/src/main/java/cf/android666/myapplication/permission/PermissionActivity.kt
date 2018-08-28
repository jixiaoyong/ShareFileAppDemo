package cf.android666.myapplication.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build.MANUFACTURER
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
import android.util.Log
import cf.android666.myapplication.R
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_permission.*

/**
 * Created by jixiaoyong on 2018/8/28.
 * email:jixiaoyong1995@gmail.com
 */
class PermissionActivity : AppCompatActivity() {


    private lateinit var mContext: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        mContext = this

       Log.d("TAG","检查ROM ${CheckRoms.isMiui()}")


        requestPermission()
//        requestPermission2()

        btn1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                SmsManager.getDefault().sendTextMessage("10010", "", "10010", null, null)
                Log.d("TAG", "send msg")
            } else {
                requestPermission()
            }
        }
    }

    //
    private val mPermissions: Array<String> = arrayOf(android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.CAMERA)
//    private val mPermissions: Array<String> = arrayOf(android.Manifest.permission.SEND_SMS)
    private val requestCodes: Array<Int> = arrayOf(1, 2, 3)
    private val descriptions: Array<String> = arrayOf("没有发送短信的权限将无法正常使用此软件","没有查看短信的权限将无法正常使用此软件",
            "没有拨打电话的权限将无法正常使用此软件")
    private var hasPermissions = arrayListOf<Boolean>()

    private fun requestPermission() {

        hasPermissions = RequestPermissionUtil.request(mContext, mPermissions,requestCodes,descriptions,
                object :(Activity,String,Boolean,Int,String)->Unit{
            override fun invoke(p1: Activity, p2: String, p3: Boolean, p4: Int, p5: String) {
                if (!p3) {
                    Log.d("TAG","没有权限，弹窗提示用户授权")
                    RequestPermissionUtil.showDialog(p1,p2,p4,p5,null)
                }
            }

        })

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array< String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        RequestPermissionUtil.onRequestPermissionsResult(mContext,requestCode,mPermissions,grantResults,
//                descriptions,object :(Activity,String,Boolean,Int,String)->Unit{
//            override fun invoke(p1: Activity, p2: String, p3: Boolean, p4: Int, p5: String) {
//                Log.d("TAG","权限$p2 通过与否 $p3")
//                var isPass = false
//
//                //如果是小米用户且Android>=6.0
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    isPass = RequestPermissionUtil.forMIUI(mContext, p2)
//                    Log.d("TAG","is pass $isPass")
//                }
//
//                if (p3 && isPass) {
//                    Log.d("TAG","有权限")
//                } else {
//                    Log.d("TAG","没有权限，弹窗提示用户授权")
//                    RequestPermissionUtil.showDialog(p1,p2,p4,
//                            "$p5 这个是假授权，只能每次都请求，嫌麻烦可以在设置中手动赋值，点确定去设置，取消等系统授权",null)
//                }
//            }
        var disPermissionArr =  arrayListOf<String>()
        var index= 0
        grantResults.map {
            if (it != PackageManager.PERMISSION_GRANTED) {
                disPermissionArr.add(permissions[index])
                index++
                Logger.d("实际的权限是" +mPermissions[index] )
            }
        }

        Logger.d("没有通过的权限是：$disPermissionArr")
    }

    fun isMIUI(): Boolean {
        var s = android.os.Build.MANUFACTURER
        Logger.d("MANUFACTURER == $MANUFACTURER")
        return "Xiaomi".equals(s)
    }
}