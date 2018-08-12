package cf.android666.myapplication.lanp2p;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import kotlin.Unit;
import kotlin.jvm.functions.Function5;

/**
 * Created by jixiaoyong on 2018/8/12.
 * email:jixiaoyong1995@gmail.com
 */
public class RequestPermissionJavaDemo extends Activity {

    private final int REQUEST_CODE = 0x01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Function5<? super Activity, ? super String, ? super Boolean, ? super Integer, ? super String, Unit> listener =
                (Function5<Activity, String, Boolean, Integer, String, Unit>) (activity, s, aBoolean, integer, s2) -> {
                    if (aBoolean) {
                        RequestPermissionUtil.INSTANCE.showDialog(activity, s, REQUEST_CODE, s2, null);
                    }
                    return null;
                };
        RequestPermissionUtil.INSTANCE.request(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new Integer[]{REQUEST_CODE},
                new String[]{"请求读写权限,主要用来读取文件列表，点击确认开始请求读写权限"},
                listener
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Function5<? super Activity, ? super String, ? super Boolean, ? super Integer, ? super String, Unit> listener =
                (Function5<Activity, String, Boolean, Integer, String, Unit>) (activity, s, aBoolean, integer, s2) -> {
                    switch (s) {
                        case Manifest.permission.READ_EXTERNAL_STORAGE:
                            if (!aBoolean) {
                                RequestPermissionUtil.INSTANCE.showDialog(activity, s, integer, s2, null);
                            }
                            break;
                        default:
                            break;
                    }
                    return null;
                };
        RequestPermissionUtil.INSTANCE.onRequestPermissionsResult(RequestPermissionJavaDemo.this, requestCode, permissions,
                grantResults, new String[]{"读取文件权限很重要，请授予权限"},
                listener);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
