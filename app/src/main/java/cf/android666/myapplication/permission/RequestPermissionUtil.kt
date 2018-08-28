package cf.android666.myapplication.permission

import android.app.Activity
import android.app.AlertDialog
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.orhanobut.logger.Logger

/**
 * Created by jixiaoyong on 2018/8/12.
 * email:jixiaoyong1995@gmail.com
 */
object RequestPermissionUtil {

//    private var mRequestCodes: Array<Int> = arrayOf()

    /**
     * 申请权限
     * shouldShowRequestPermissionRationale 是否需要向用户解释权限用途
     */
    fun request(activity: Activity, permissions: Array<String>, requestCodes: Array<Int>,
                @Nullable descriptions: Array<String>,
                @Nullable showDescriptionsListener: ((activity: Activity, permission: String,
                                                      shouldShowRequestPermissionRationale: Boolean, requestCode: Int, description: String) -> Unit)?): ArrayList<Boolean> {
        var result = ArrayList<Boolean>(permissions.size)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            result.fill(true)
            return result
        } else {

            if (!checkPermission(activity,permissions)) {
                ActivityCompat.requestPermissions(activity, permissions, 1)
            }

//            for (i in 0 until permissions.size) {
//                if (ContextCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
//                    result.add(forMIUI(activity, permissions[i]))
//                    Log.d("TAG","forMIUI(activity, permissions[i]) : ${forMIUI(activity, permissions[i])}")
//                } else {
//                    result.add(false)
//                    //不允许
//                    val shouldShowDialog = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])
//                    Logger.d("需要解释一下为什么申请这个权限: $shouldShowDialog + permission ${permissions[i]}")
//                    showDescriptionsListener?.invoke(activity, permissions[i], shouldShowDialog, requestCodes[i], descriptions[i])
//                    val isFirst = false
//                    if (shouldShowDialog || isFirst) {
//                        // 还需要判断一下用户是否选择了不在询问，可在调用处处理
//                        // 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
//                        // 如果设备规范禁止应用具有该权限，此方法也会返回 false。
//                        // 当进行第二次权限请求被拒绝并且shouldShowRequestPermissionRationale（）返回false时，那么该用户一定是选择了“不再提示”这一选项
//                        ActivityCompat.requestPermissions(activity, arrayOf(permissions[i]), requestCodes[i])
//                        Log.d("TAG", "再次请求权限 ${permissions[i]}")
//                    }
//
//
//                }
//            }
            return result
        }

    }

    private fun checkPermission(activity: Activity,permissions: Array<String>): Boolean {
        permissions.map {
            if (ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun forMIUI(context: Context, permission: String): Boolean {
        try {
            var appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var opsName: String? = AppOpsManager.permissionToOp(permission) ?: return true
            var opsMode = appOpsManager.checkOpNoThrow(opsName, Process.myUid(),
                    context.packageName)
            return opsMode == AppOpsManager.MODE_ALLOWED

        } catch (ex: Exception) {
            ex.printStackTrace()
            return true
        }
    }

    /**
     * 请求权限后的回调方法
     */
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<String>, @NonNull grantResults: IntArray?,
                                   @Nullable descriptions: Array<String>,
                                   @Nullable listener: ((Activity, String, Boolean, Int, String) -> Unit)?) {

        for (i in 0 until permissions.size) {

            listener?.invoke(activity, permissions[i], (grantResults?.isNotEmpty() ?: false)
                    && grantResults?.get(0) == PackageManager.PERMISSION_GRANTED, requestCode, descriptions[i])

        }
    }

    /** The identifier for the positive button.  */
    const val BUTTON_POSITIVE = -1

    /** The text for the positive button.  */
    const val BUTTON_POSITIVE_TEXT = "确认"

    /** The identifier for the negative button.  */
    const val BUTTON_NEGATIVE = -2

    /** The text for the negative button.  */
    const val BUTTON_NEGATIVE_TEXT = "取消"

    /** The title of dialog **/
    const val DIALOG_TITLE = "请求权限"

    fun showDialog(activity: Activity, permission: String, requestCode: Int, description: String,
                   @Nullable onButtonClickListener: ((whichButton: Int, activity: Activity, permission: String, requestCode: Int, description: String) -> Unit)?) {

        var builder = AlertDialog.Builder(activity)
        builder.setTitle(DIALOG_TITLE)
        builder.setMessage(description)

        val dialog = builder.create()
        dialog.setOnCancelListener {
            dialog.dismiss()
        }
        dialog.setButton(BUTTON_POSITIVE, BUTTON_POSITIVE_TEXT) { mDialog, which ->
            if (which == BUTTON_POSITIVE) {
                mDialog.dismiss()
                ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
                onButtonClickListener?.invoke(BUTTON_POSITIVE, activity, permission, requestCode, description)
            }
        }
        dialog.setButton(BUTTON_NEGATIVE, BUTTON_NEGATIVE_TEXT) { mDialog, which ->
            if (which == BUTTON_NEGATIVE) {
                mDialog.dismiss()
                onButtonClickListener?.invoke(BUTTON_NEGATIVE, activity, permission, requestCode, description)
            }
        }
        dialog.show()
    }
}
