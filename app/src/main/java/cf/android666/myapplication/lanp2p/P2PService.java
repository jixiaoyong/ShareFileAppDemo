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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static cf.android666.myapplication.lanp2p.NetUtils.Info.PORT;

/**
 * 服务器
 * Created by jixiaoyong on 2018/7/26.
 * email:jixiaoyong1995@gmail.com
 */
public class P2PService extends Service {

    private static boolean SERVER_RUN = true;
    private ServerListener listener;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("P2PService onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("P2PService onCreate");

        new Thread(() -> startServer()).start();
    }

    private void startServer() {
        Logger.d("socket = startServer");

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (SERVER_RUN) {
                Socket socket = serverSocket.accept();
                Logger.d("socket = " + socket);
                InetAddress s1 = socket.getInetAddress();
                Logger.d("s " + s1.getHostAddress());
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();
                String s = "";
                while ((s = bufferedReader.readLine()) != null) {
                    stringBuffer.append(s);
                }

                Logger.d("stirng is " + stringBuffer.toString());
                if (listener != null) {
                    listener.onServerAccept(stringBuffer.toString());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    interface ServerListener {

        void onServerAccept(String s);
    }
}
