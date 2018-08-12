package cf.android666.myapplication.lanp2p

import android.Manifest
import android.app.Activity
import android.os.Bundle

/**
 * Created by jixiaoyong on 2018/8/11.
 * email:jixiaoyong1995@gmail.com
 */
public class RequestPermissionkotlinDemo : Activity() {

    lateinit var  mContext: Activity
    val REQUEST_CODE = 0x01

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        RequestPermissionUtil.request(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                arrayOf(REQUEST_CODE),
                arrayOf("请求读写权限,主要用来读取文件列表，点击确认开始请求读写权限"),
                object : (Activity, String, Boolean, Int, String) -> Unit {
                    override fun invoke(activity: Activity, permission: String,
                                        shouldShowRequestPermissionRationale: Boolean,
                                        requestCode: Int, description: String) {
                        //是否需要解释权限用途
                        if (shouldShowRequestPermissionRationale) {
                            RequestPermissionUtil.showDialog(mContext,permission,requestCode,description,
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
                }
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        RequestPermissionUtil.onRequestPermissionsResult(mContext, requestCode, permissions,
                grantResults, arrayOf("读取文件权限很重要，请授予权限"),
                object :(Activity,String?,Boolean,Int,String)->Unit{
                    override fun invoke(p1: Activity, p2: String?, p3: Boolean, p4: Int, p5: String) {
                        when (p2) {
                            Manifest.permission.READ_EXTERNAL_STORAGE->{
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