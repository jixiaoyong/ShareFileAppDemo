package cf.android666.myapplication.web

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by jixiaoyong on 2018/8/25.
 * email:jixiaoyong1995@gmail.com
 */
object Utils {


    fun loadManifest(context: Context, key: String): String {
        var result: String

        var appInfo= context.applicationContext.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        result = appInfo?.metaData?.getString(key).toString()

        return result;
    }
}