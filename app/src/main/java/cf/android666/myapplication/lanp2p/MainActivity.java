package cf.android666.myapplication.lanp2p;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cf.android666.myapplication.R;

import static cf.android666.myapplication.lanp2p.NetUtils.Info.PORT;

/**
 * Created by jixiaoyong on 2018/7/26.
 * email:jixiaoyong1995@gmail.com
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private TextView textView;
    private RecyclerView recyclerView;
    private EditText editTextMsg;

    private String host = "";
    private String mLocalHost = "";

    private List<String> msgList;
    private RecyclerFileAdapter mRecyclerAdapter;
    private File mSelectFile;

    private static final int UPDATE_HOST = 0;
    private static final int UPDATE_LIST = 1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_HOST:
                    if (textView != null) {
                        textView.setText(mLocalHost + "\n" + host);
                        Logger.d("update host + PORT " + host + ":" + PORT);
                    }
                    break;
                case UPDATE_LIST:
                    mRecyclerAdapter.notifyDataSetChanged();
                    Logger.d("update list " + msgList.size());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lan_layout);

        mContext = MainActivity.this;

        msgList = new ArrayList<>();
        mLocalHost = "local host:" + NetUtils.getIp();

        findViewById(R.id.start_server).setOnClickListener(this);
        findViewById(R.id.start_client).setOnClickListener(this);
        findViewById(R.id.clean).setOnClickListener(this);
        editTextMsg = findViewById(R.id.edit_msg);
        textView = findViewById(R.id.text);
        textView.setText(mLocalHost);

        final List<String> files = FileUtils.getSdFilePath(this);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerAdapter = new RecyclerFileAdapter(mContext, msgList);
        mRecyclerAdapter.setmListener(new RecyclerFileAdapter.MListener() {
            @Override
            public void onClick(int position) {
                if (files != null) {
                    String filepath = FileUtils.getSdDir(mContext) + File.separator + files.get(position);
                    File file = new File(filepath);
                    Log.d("tag", "mainactivity " + filepath);
                    if (file.exists() && file.isFile()) {
                        mSelectFile = file;
                        Log.d("tag", "mSelectFile = file; ");
                        startSend();
                    } else if (file.exists() && file.isDirectory()) {
                        msgList.clear();
                        msgList.addAll(Arrays.asList(file.list()));
                        mRecyclerAdapter.notifyDataSetChanged();
                        Log.d("tag", "mSelectFile = isDirectory; msgList" + msgList.size());
                    }
                }
            }
        });
        recyclerView.setAdapter(mRecyclerAdapter);

//        if (files != null) {
//            msgList.clear();
//            msgList.addAll(files);
//            mRecyclerAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_server:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            startServer();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.start_client:
                startSend();
                break;
            case R.id.clean:
                msgList.clear();
                mRecyclerAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void startSend() {
        final String host = ((EditText) findViewById(R.id.edit_host)).getText().toString();
        if ("".equals(host)) {
            Toast.makeText(mContext, "host can't be empty!", Toast.LENGTH_SHORT).show();
           return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.d("start client onClick");
                startServer(host);
            }
        }).start();
    }

    //服务器
    private void startServer() throws InterruptedException {
        Logger.d("socket = startServer");

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
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
                String s2 = stringBuffer.toString() + "\n";
                Logger.d("stirng is " + s2);

                host = "client host :" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort();
                handler.sendEmptyMessage(UPDATE_HOST);
                msgList.add("server rec:" + getCurrentTime() + "\n-------" + s2);
                handler.sendEmptyMessage(UPDATE_LIST);

//                DataInputStream dataInputStream = new DataInputStream(inputStream);
//                File file = new File(FileUtils.getSdDir(mContext) + File.separator + dataInputStream.readUTF());
//
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                byte[] bytes = new byte[1024];
//                int length = -1;
//                while ((length = dataInputStream.read(bytes)) != -1) {
//                    fileOutputStream.write(bytes, 0, length);
//                    fileOutputStream.flush();
//                    Log.d("tag", "*************save");
//                }
//
//                Log.d("tag","file name  length " +
//                dataInputStream.readUTF() + dataInputStream.readLong()
//                + file.getAbsolutePath());

                Thread.sleep(500);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SimpleDateFormat simpleDateFormat;
    private String getCurrentTime() {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        }
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    //客户端
    private void startServer(String host) {
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
            String input = editTextMsg.getText().toString();
            if ("".equals(input)) {
                input = "c:hello from client！时间：" + getCurrentTime();
            } else {
                input += getCurrentTime();
            }


//            if (mSelectFile != null) {
//
//                FileInputStream fileInputStream= new FileInputStream(mSelectFile);
//                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//                dataOutputStream.writeUTF(mSelectFile.getName());
//                dataOutputStream.writeLong(mSelectFile.length());
//                int length = -1;
//                byte[] bytes = new byte[1024];
//                while ((length = fileInputStream.read(bytes)) != -1) {
//                    dataOutputStream.write(bytes, 0, length);
//                    dataOutputStream.flush();
//                }
//                Log.d("tag", "send all");
//                return;
//            }
//
            outputStreamWriter.write(input);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            Logger.d("socket send hello end");

            msgList.add("client send:" + getCurrentTime() + "\n*******" + input);
            handler.sendEmptyMessage(UPDATE_LIST);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
