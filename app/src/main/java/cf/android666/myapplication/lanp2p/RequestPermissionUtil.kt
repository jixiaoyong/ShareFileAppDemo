package cf.android666.myapplication.lanp2p

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.orhanobut.logger.Logger

/**
 * Created by jixiaoyong on 2018/8/12.
 * email:jixiaoyong1995@gmail.com
 */
object RequestPermissionUtil {

    private var mRequestCodes: Array<Int> = arrayOf()

    /**
     * 申请权限
     * shouldShowRequestPermissionRationale 是否需要向用户解释权限用途
     */
    fun request(activity: Activity, permissions: Array<String>, requestCodes: Array<Int>,
                @Nullable descriptions: Array<String>,
                @Nullable showDescriptionsListener: ((activity: Activity, permission: String,
                                                      shouldShowRequestPermissionRationale: Boolean, requestCode: Int, description: String) -> Unit)?): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        mRequestCodes = requestCodes

        for (i in 0 until permissions.size) {
            if (ContextCompat.checkSelfPermission(activity, permissions[i])
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                //不允许
                val shouldShowDialog = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])
                Logger.d("需要解释一下为什么申请这个权限: $shouldShowDialog")
                showDescriptionsListener?.invoke(activity, permissions[i], shouldShowDialog, requestCodes[i], descriptions[i])
                if (!shouldShowDialog) {
                    // 还需要判断一下用户是否选择了不在询问，可在调用处处理
                    // 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
                    // 如果设备规范禁止应用具有该权限，此方法也会返回 false。
                    // 当进行第二次权限请求被拒绝并且shouldShowRequestPermissionRationale（）返回false时，那么该用户一定是选择了“不再提示”这一选项
                    ActivityCompat.requestPermissions(activity, arrayOf(permissions[i]), requestCodes[i])
                }
                return false
            }
        }
        return true
    }

    /**
     * 请求缺陷后的回调方法
     */
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int,  permissions: Array<String>?, @NonNull grantResults: IntArray?,
                                   @Nullable descriptions: Array<String>,
                                   @Nullable listener: ((Activity, String?, Boolean, Int, String) -> Unit)?) {

        for (i in 0 until mRequestCodes.size) {
            if (requestCode == mRequestCodes[i]) {
                listener?.invoke(activity, permissions?.get(i), (grantResults?.isNotEmpty() ?: false) && grantResults?.get(0) == PackageManager.PERMISSION_GRANTED, requestCode, descriptions[i])
            }
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
                   @Nullable onButtonClickListener: ((whichButton:Int,activity: Activity, permission: String, requestCode: Int, description: String) -> Unit)?) {

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
                onButtonClickListener?.invoke(BUTTON_POSITIVE,activity, permission, requestCode, description)
            }
        }
        dialog.setButton(BUTTON_NEGATIVE, BUTTON_NEGATIVE_TEXT) { mDialog, which ->
            if (which == BUTTON_NEGATIVE) {
                mDialog.dismiss()
                onButtonClickListener?.invoke(BUTTON_NEGATIVE,activity, permission, requestCode, description)
            }
        }
        dialog.show()
    }
}
