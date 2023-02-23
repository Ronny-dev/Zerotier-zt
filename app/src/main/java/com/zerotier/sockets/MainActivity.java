package com.zerotier.sockets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Ronny";

    private static final long networkId = 0x0106d7f4625ca3d1L;

    private boolean isSocketRunning = false;

    private ZeroTierNode node;

    private ZeroTierDatagramSocket udpSocket = null;

    private String mZerotierConfigPath;

    private final String remoteUdpSocketTarget = "10.134.79.135";

    private final int remoteUdpSocketTargetPort = 9993;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_connect).setOnClickListener(this);
        findViewById(R.id.btn_received).setOnClickListener(this);
        findViewById(R.id.btn_disconnect).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);

        mZerotierConfigPath = getFilesDir().getPath();
    }

    private void connectZeroTier() {
        node = new ZeroTierNode();

        node.initFromStorage(mZerotierConfigPath);

        node.start();

        Log.i(TAG, "Wait Zerotier Online...");

        while (!node.isOnline()) {
            ZeroTierNative.zts_util_delay(50);
        }

        Log.i(TAG, "Node ID: " + Long.toHexString(node.getId()));

        Log.i(TAG, "Joining network...");

        node.join(networkId);

        while (!node.isNetworkTransportReady(networkId)) {
            ZeroTierNative.zts_util_delay(50);
        }

        try {
            udpSocket = new ZeroTierDatagramSocket();

            Log.i(TAG, "join!!!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg() {
        if (node != null && node.isOnline()) {

            Log.i(TAG, "send msg");
            new Thread(() -> {
                try {

                    udpSocket.connect(remoteUdpSocketTarget, remoteUdpSocketTargetPort);

                    byte[] msg = "Hello".getBytes(StandardCharsets.UTF_8);

                    DatagramPacket packet = new DatagramPacket(msg, 0, msg.length, InetAddress.getByName(remoteUdpSocketTarget), remoteUdpSocketTargetPort);

                    udpSocket.send(packet);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void receivedZeroTier() {
        if (node != null && node.isOnline()) {

            isSocketRunning = true;

            Log.i(TAG, "start received thread.");
            
            new Thread(() -> {
                try {
                    udpSocket = new ZeroTierDatagramSocket();

                    udpSocket.setReuseAddress(true);

                    udpSocket.bind("0.0.0.0", 9994);

                    byte[] bytes = new byte[4096];

                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

                    while (isSocketRunning) {

                        Log.i(TAG, "wait...");

                        udpSocket.receive(packet);

                        Log.i(TAG, "pack: " + HexStrConvertUtil.toHex(packet.getData()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void disconnectZeroTier() {
        Log.i(TAG, "disconnect.");
        isSocketRunning = false;
        if (udpSocket != null) {
            try {
                udpSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (node != null) {
            node.leave(networkId);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                connectZeroTier();
                break;
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.btn_received:
                receivedZeroTier();
                break;
            case R.id.btn_disconnect:
                disconnectZeroTier();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        disconnectZeroTier();
        super.onDestroy();
    }
}