package com.rds.ftms.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtil {
    private final static String TAG = "PropertiesUtil";

    private Context mContext;
    private String mPath;
    private String mFile;
    private Properties mProp;
    private static PropertiesUtil mPropUtil = null;

    public static PropertiesUtil getInstance(Context context) {
        if (mPropUtil == null) {
            mPropUtil = new PropertiesUtil();
            mPropUtil.mContext = context;
            mPropUtil.mPath = Environment.getExternalStorageDirectory() + File.separator + "ExmKeyValue";
            mPropUtil.mFile = "properties1.ini";
        }
        return mPropUtil;
    }

    /**
     * 设置路径
     * @param path
     * @return
     */
    public PropertiesUtil setPath(String path) {
        mPath = path;
        return this;
    }

    /**
     * 设置文件名
     * @param file
     * @return
     */
    public PropertiesUtil setFile(String file) {
        mFile = file;
        return this;
    }

    public PropertiesUtil init() {
        LogUtil.d(TAG, "path="+mPath+File.separator +mFile);
        try {
            File dir = new File(mPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, mFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            InputStream is = new FileInputStream(file);
            mProp = new Properties();
            mProp.load(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void commit() {
        try {
            File file = new File(mPath + File.separator +  mFile);
            OutputStream os = new FileOutputStream(file);
            mProp.store(os, "");
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mProp.clear();
    }

    public void clear() {
        mProp.clear();
    }

    public void open() {
        mProp.clear();
        try {
            File file = new File(mPath + File.separator +  mFile);
            InputStream is = new FileInputStream(file);
            mProp = new Properties();
            mProp.load(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeString(String name, String value) {
        mProp.setProperty(name, value);
    }

    public String readString(String name, String defaultValue) {
        return mProp.getProperty(name, defaultValue);
    }

    public void writeInt(String name, int value) {
        mProp.setProperty(name, ""+value);
    }

    public int readInt(String name, int defaultValue) {
        return Integer.parseInt(mProp.getProperty(name, ""+defaultValue));
    }

    public void writeBoolean(String name, boolean value) {
        mProp.setProperty(name, ""+value);
    }

    public boolean readBoolean(String name, boolean defaultValue) {
        return Boolean.parseBoolean(mProp.getProperty(name, ""+defaultValue));
    }

    public void writeDouble(String name, double value) {
        mProp.setProperty(name, ""+value);
    }

    public double readDouble(String name, double defaultValue) {
        return Double.parseDouble(mProp.getProperty(name, ""+defaultValue));
    }

}