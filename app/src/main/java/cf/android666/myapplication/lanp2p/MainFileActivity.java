package cf.android666.myapplication.lanp2p;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cf.android666.myapplication.R;
import cf.android666.myapplication.qrcode.MainActivity;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import kotlin.Unit;
import kotlin.jvm.functions.Function5;

import static cf.android666.myapplication.lanp2p.NetUtils.Info.PORT;

/**
 * Created by jixiaoyong on 2018/7/26.
 * email:jixiaoyong1995@gmail.com
 */
public class MainFileActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private TextView textView;
    private ImageView mImageView;
    private RecyclerView recyclerView;
    private EditText editTextMsg;
    private EditText hostEditText;
    private TextView progress;

    private String host = "";
    private String mLocalHost = "";

    private List<String> msgList;
    private RecyclerFileAdapter mRecyclerAdapter;
    private File mSelectFile;

    private static final int UPDATE_HOST = 0;
    private static final int UPDATE_LIST = 1;
    private static final int UPDATE_PROGRESS = 2;
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
                case UPDATE_PROGRESS:
                    if (progress != null) {
                        progress.setText(msg.obj.toString());
                        Log.d("tag", "update list " + msgList.size());
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private final int REQUEST_CODE = 0x01;
    private final int ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lan_file_layout);

        mContext = MainFileActivity.this;

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

        msgList = new ArrayList<>();

        findViewById(R.id.start_server).setOnClickListener(this);
        findViewById(R.id.start_client).setOnClickListener(this);
        findViewById(R.id.scan).setOnClickListener(this);
        findViewById(R.id.clean).setOnClickListener(this);
        mImageView = findViewById(R.id.qr_code);
        editTextMsg = findViewById(R.id.edit_msg);
        textView = findViewById(R.id.text);
        progress = findViewById(R.id.progress);
        hostEditText =  findViewById(R.id.edit_host);

        mLocalHost = "local host:" + NetUtils.getIp();
        textView.setText(mLocalHost);
        showQRCode(NetUtils.getIp());

        final List<String> files = FileUtils.getSdFilePath(this);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerAdapter = new RecyclerFileAdapter(mContext, msgList);
        mRecyclerAdapter.setmListener(position -> {
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
        });
        recyclerView.setAdapter(mRecyclerAdapter);

        if (files != null) {
            msgList.clear();
            msgList.addAll(files);
            mRecyclerAdapter.notifyDataSetChanged();
        }




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
        RequestPermissionUtil.INSTANCE.onRequestPermissionsResult(MainFileActivity.this, requestCode, permissions,
                grantResults, new String[]{"读取文件权限很重要，请授予权限"},
                listener);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            case R.id.scan:
                startActivityForResult(new Intent(mContext, MainActivity.class),ACTIVITY_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private void showQRCode(String host) {
        new Thread(() -> {
            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(host, BGAQRCodeUtil.dp2px(mContext, 150F));
            runOnUiThread(() -> mImageView.setImageBitmap(bitmap));
        }).start();
    }

    private void startSend() {
        final String host = hostEditText.getText().toString();
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
            Logger.w("服务器启动：" + serverSocket.isBound());
            Message message = handler.obtainMessage(UPDATE_PROGRESS);
            message.obj = "服务器启动：" + serverSocket.isBound();
            handler.sendMessage(message);

            while (true) {
                Socket socket = serverSocket.accept();
                Logger.d("socket = " + socket);
                InetAddress s1 = socket.getInetAddress();
                Logger.d("s " + s1.getHostAddress());
                InputStream inputStream = socket.getInputStream();

                DataInputStream dataInputStream = new DataInputStream(inputStream);
                File file = new File(FileUtils.getSdDir(mContext) + File.separator + dataInputStream.readUTF());
                long size = dataInputStream.readLong();

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int length = -1;
                int total = 0;
                while ((length = dataInputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, length);
                    fileOutputStream.flush();
                    Log.d("tag", "*************save");
                    total += length;
                    String progressStr = file.getAbsolutePath()
                            + "**********save progress" + (total * 100 / size) + "%";
                    Message msg = handler.obtainMessage(UPDATE_PROGRESS);
                    msg.obj = progressStr;
                    handler.sendMessage(msg);
                    Log.d("tag", progressStr);

                }

                Log.d("tag", "file name  length " + file.getAbsolutePath());

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

            if (mSelectFile != null) {

                FileInputStream fileInputStream = new FileInputStream(mSelectFile);
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(mSelectFile.getName());
                dataOutputStream.writeLong(mSelectFile.length());
                int length = -1;
                byte[] bytes = new byte[1024];
                int total = 0;
                while ((length = fileInputStream.read(bytes)) != -1) {
                    dataOutputStream.write(bytes, 0, length);
                    dataOutputStream.flush();

                    total += length;
                    String progressStr = "**********send progress" + (total * 100 / mSelectFile.length())
                            + " % selectfile" + mSelectFile.getAbsolutePath();
                    Message msg = handler.obtainMessage(UPDATE_PROGRESS);
                    msg.obj = progressStr;
                    handler.sendMessage(msg);
                    Log.d("tag", progressStr);
                }
                Log.d("tag", "**********send all");
                return;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ACTIVITY_REQUEST_CODE) {
            String s = data.getStringExtra("host");
            hostEditText.setText(s);
        }
    }
}
