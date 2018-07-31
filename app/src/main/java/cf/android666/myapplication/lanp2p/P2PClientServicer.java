package cf.android666.myapplication.lanp2p;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static cf.android666.myapplication.lanp2p.NetUtils.Info.PORT;

/**
 * 服务器
 * Created by jixiaoyong on 2018/7/26.
 * email:jixiaoyong1995@gmail.com
 */
public class P2PClientServicer extends Service {

    private static boolean SERVER_RUN = true;
    private ClientListener listener;
    private String host;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        host = intent.getStringExtra("host");
        Logger.d("P2PClientServicer onBind" + host);

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("P2PClientServicer onCreate");

        new Thread(new Runnable() {
            @Override
            public void run() {
                startServer();
            }
        }).start();
    }

    private void startServer() {
        Logger.d("P2PClientServicer startServer");

        if (host == null || "".equals(host)) {
            Logger.e("error: wrong host");
            return;
        }

        try {
            Socket socket = new Socket(host, PORT);
            Logger.d("socket = " + socket);

            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write("hello from client！");
            outputStreamWriter.flush();
            outputStreamWriter.close();

            Logger.d("socket send hello end");
//            InputStream inputStream = socket.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//            StringBuffer stringBuffer = new StringBuffer();
//            String s = "";
//            while ((s = bufferedReader.readLine()) != null) {
//                stringBuffer.append(s);
//            }
//
//            Logger.d("stirng is " + stringBuffer.toString());
//            if (listener != null) {
//                listener.onClientAccept(stringBuffer.toString());
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setListener(ClientListener listener) {
        this.listener = listener;
    }

    interface ClientListener {

        void onClientAccept(String s);
    }
}
