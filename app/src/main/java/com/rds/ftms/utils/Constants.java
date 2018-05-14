package com.rds.ftms.utils;

import android.os.Environment;

import java.io.File;

public class Constants {
    public final static String DB = "jzdjs_family.db";
    //配置文件默认路径
    public final static  String CONFIG_PATH = Environment.getExternalStorageDirectory() + File.separator + "ExmKeyValue";;
    //配置文件默认名称
    public final static  String CONFIG_FILE = "properties1.ini";
}