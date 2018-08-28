package cf.android666.myapplication;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.database.Database;

import java.time.temporal.ValueRange;

import cf.android666.myapplication.greendao.generate.DaoMaster;
import cf.android666.myapplication.greendao.generate.DaoSession;
import kotlin.LateinitKt;

/**
 * Created by jixiaoyong on 2018/7/26.
 * email:jixiaoyong1995@gmail.com
 */
public class MyApplication extends Application {

    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "users_database");
        Database database = devOpenHelper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }
}
