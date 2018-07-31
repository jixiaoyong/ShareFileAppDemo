package cf.android666.myapplication.lanp2p;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jixiaoyong on 2018/7/30.
 * email:jixiaoyong1995@gmail.com
 */
public class FileUtils {

    public static List<String> getSdFilePath(Context context) {
        String sdPath = getSdDir(context);
        if (sdPath == null) {
            return null;
        }
        return Arrays.asList(getDirFilePath(sdPath));
    }

    public static String getSdDir(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return null;
        }
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return sdPath;
    }

    public static String[] getDirFilePath(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            return file.list();
        } else if (file.exists()&&file.isFile()){
            return new String[]{file.getAbsolutePath()};
        }
        return null;
    }
}
