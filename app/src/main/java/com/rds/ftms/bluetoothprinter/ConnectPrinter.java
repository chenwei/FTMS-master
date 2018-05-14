package com.rds.ftms.bluetoothprinter;

/**
 * Created by Administrator on 2017/9/14.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Handler;


/**
     * 保持蓝牙连接的线程
     */
    public class ConnectPrinter extends Thread {

        // 固定不变,表示蓝牙通用串口服务
        private static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        private BluetoothSocket mSocket; // 蓝牙连接的socket

        private OutputStream mOutputStream;   // 用于打印数据的输出流

        private InputStream mInputStream;

        private Handler mHandler;

        private byte[] readBuf = new byte[1024];

        private BluetoothDevice mDevice;

        private String printerstatus = "";

        public void ConnectThreadAddress(String address) throws Exception {

            BluetoothDevice device;
            BluetoothAdapter mBluetoothAdapter;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            device = mBluetoothAdapter.getRemoteDevice(address);
            try {
                mSocket = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);  // 创建连接的Socket对象
                mSocket.connect();
                this.mOutputStream = this.mSocket.getOutputStream();
                this.mInputStream = this.mSocket.getInputStream();

            } catch (IOException var7) {
                try {
                    this.mSocket.close();
                } catch (IOException var6) {
                    ;
                }
            }

        }

    public void sendcommand(byte[] message) {
        try {
            this.mOutputStream.write(message);
        } catch (IOException var3) {
            Log.e("", "发送异常.", var3);
        }

    }

    public void sendcommand(String message) {
        byte[] msgBuffer = message.getBytes();

        try {
            this.mOutputStream.write(msgBuffer);
        } catch (IOException var4) {
            Log.e("", "发送异常.", var4);
        }

    }

    public void closeport() {
        try {
            this.mSocket.close();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
   public String stutas(){
       byte[] message = new byte[]{(byte)0x1B, (byte)0x21, (byte)0x3F};
       try {
           this.mOutputStream.write(message);
       } catch (IOException var4) {
           Log.e("", "打印异常.", var4);
       }
       try {
           Thread.sleep(1000L);
       } catch (InterruptedException var3) {
           var3.printStackTrace();
       }
       try {
           while(this.mInputStream.available() > 0) {
               this.readBuf = new byte[1];
              this.mInputStream.read(this.readBuf,0,1);
           }
       } catch (IOException var5) {
           var5.printStackTrace();
       }
       return Arrays.toString(this.readBuf);
   }
    }

