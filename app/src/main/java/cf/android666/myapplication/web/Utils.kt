package cf.android666.myapplication.web

import android.content.Context
import android.content.pm.PackageManager
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

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

    @Throws(IOException::class)
    fun getStringFromInputStream(`is`: InputStream): String {
        val os = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = -1
        while (true) {
            len = `is`.read(buffer)
            if (len == -1) {
                break
            }
            os.write(buffer, 0, len)
        }
        `is`.close()
        val state = os.toString() //把字节流转字符串
        os.close()
        return state
    }
}