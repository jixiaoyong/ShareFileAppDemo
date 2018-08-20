package cf.android666.myapplication.qrcode

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import cf.android666.myapplication.R
import cf.android666.myapplication.lanp2p.RequestPermissionUtil
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import kotlinx.android.synthetic.main.activity_qrcode.*


/**
 * Created by jixiaoyong on 2018/8/20.
 * email:jixiaoyong1995@gmail.com
 */
class MainActivity : AppCompatActivity(), QRCodeView.Delegate {
    private lateinit var mContext:Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        mContext = this

        requestPermission()

        zxing_view.setDelegate(this)

        image.setOnClickListener {
            Thread {
                var bitmap = QRCodeEncoder.syncEncodeQRCode(edit_text.text.toString(), BGAQRCodeUtil.dp2px(mContext, 150F))
                runOnUiThread{
                    image.setImageBitmap(bitmap)
                }
            }.start()
        }
    }

    override fun onStart() {
        super.onStart()
        zxing_view.startCamera()
        zxing_view.startSpotAndShowRect()
    }

    override fun onStop() {
        zxing_view.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        zxing_view.onDestroy()
        super.onDestroy()
    }

    //震动
    fun vibrate() {
        var vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    override fun onScanQRCodeSuccess(result: String?) {
        edit_text.setText(result)
        vibrate()
        zxing_view.startSpot()
        intent.putExtra("host", result)
        setResult(1,intent)
        finish()
    }

    override fun onScanQRCodeOpenCameraError() {

    }

    private fun requestPermission() {

        var hasPermission = RequestPermissionUtil.request(this
        , arrayOf(Manifest.permission.CAMERA), arrayOf(1), arrayOf("请求相机权限"),
                object :(Activity,String,Boolean,Int,String)->Unit{
                    override fun invoke(p1: Activity, p2: String, p3: Boolean, p4: Int, p5: String){
                        //是否需要解释权限用途
                        if (p3) {
                            RequestPermissionUtil.showDialog(mContext, p2,p4,p5,
                                    object :(Int,Activity,String,Int,String)->Unit{
                                        override fun invoke(p1: Int, p2: Activity, p3: String, p4: Int, p5: String) {
                                            when(p1) {
                                                RequestPermissionUtil.BUTTON_POSITIVE->{

                                                }
                                                RequestPermissionUtil.BUTTON_NEGATIVE->{

                                                }
                                            }
                                        }
                                    })
                        }
                    }
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        RequestPermissionUtil.onRequestPermissionsResult(mContext,requestCode,permissions,grantResults, arrayOf("摄像权限"),
                object :(Activity,String?,Boolean,Int,String)->Unit{
                    override fun invoke(p1: Activity, p2: String?, p3: Boolean, p4: Int, p5: String) {
                        when (p2) {
                            Manifest.permission.CAMERA->{
                                if (p3) {
                                    //请求通过了
                                } else {
                                    RequestPermissionUtil.showDialog(mContext,p2,p4,p5,
                                            object :(Int, Activity, String,Int, String) -> Unit {
                                                override fun invoke(p1: Int, p2: Activity, p3: String, p4: Int, p5: String) {
                                                    when(p1) {
                                                        RequestPermissionUtil.BUTTON_POSITIVE->{

                                                        }
                                                        RequestPermissionUtil.BUTTON_NEGATIVE->{

                                                        }
                                                    }
                                                }
                                            })
                                }
                            }
                        }
                    }

                })
    }
}