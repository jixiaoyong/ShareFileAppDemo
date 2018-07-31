package cf.android666.myapplication;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by jixiaoyong on 2018/7/26.
 * email:jixiaoyong1995@gmail.com
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());

    }
}
