package com.rds.ftms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tscdll.TSCActivity;
import com.rds.ftms.bean.FtmsFamilyMemberBean;
import com.rds.ftms.bluetoothprinter.DeviceListActivity;
import com.rds.ftms.dao.FtmsFamilyDao;
import com.rds.ftms.dao.FtmsFamilyMemberDao;
import com.rds.ftms.fragment.FamilyMemberPrintFragment;
import com.rds.ftms.fragment.FragmentFamilyAdd;
import com.rds.ftms.fragment.FragmentFamilySearch;
import com.rds.ftms.fragment.UserFragment;
import com.rds.ftms.utils.Constants;
import com.rds.ftms.utils.LogUtil;
import com.rds.ftms.utils.PropertiesUtil;

import java.sql.SQLException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final TSCActivity printUtils = new TSCActivity();
    private static final int REQUEST_CODE = 101;

    /**
     * 打开蓝牙端口
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("tag", "onActivityResult" + "requestCode" + requestCode + "\n resultCode=" + resultCode);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                try {
                    /**
                     * 打印机打开蓝牙端口
                     */
                    printUtils.openport(result);
                    Toastshow("已连接");
//                    menuItem.setTitle("蓝牙打印机(已连接)");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toastshow("连接异常");
//                    menuItem.setTitle("蓝牙打印机(连接异常)");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Toastshow(String str) {

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 初次安装放开注释，更新版本请注释
         *
         */
        initParams();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setCurrentFragment();

//        FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(getApplicationContext());
//        try{
//            dao.update1();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//
//        List<FtmsFamilyMemberBean> list = dao.queryAll();
//       int aa = (1==1?0:1);

    }

    private void setCurrentFragment() {
        //家系创建
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentFamilyAdd familyAdd = FragmentFamilyAdd.newInstance();
        transaction.addToBackStack(null);
        transaction.replace(R.id.frame_content, familyAdd).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            setCurrentFragment();
        } else if (id == R.id.nav_gallery) {
            //家系创建
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragmentFamilySearch familyAdd = FragmentFamilySearch.newInstance();
            transaction.addToBackStack(null);
            transaction.replace(R.id.frame_content, familyAdd).commit();
        } else if (id == R.id.nav_slideshow) {
            try {
                FtmsFamilyDao familyDao = new FtmsFamilyDao(this);
                familyDao.ExportToCSV("family-info.csv");
                FtmsFamilyMemberDao familymemberDao = new FtmsFamilyMemberDao(this);
                familymemberDao.ExportToCSV("family-members-info.csv");

                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("数据导出成功");
                pDialog.setCancelable(false);
                pDialog.show();
            } catch (Exception e) {
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("数据导出异常");
                pDialog.setContentText("数据导出异常，请联系技术人员!"/*+"请在主菜单栏选择【蓝牙打印机】设置"*/);
                pDialog.setCancelable(false);
                pDialog.show();
            }
        } else if (id == R.id.nav_manage) {
            //蓝牙设置
            //item.setTitle("蓝牙打印机(已连接)");
            Intent intent = new Intent(MainActivity.this,
                    DeviceListActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (id == R.id.nav_share) {
            //人员查询打印
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            UserFragment userFragment = UserFragment.newInstance();
            transaction.addToBackStack(null);
            transaction.replace(R.id.frame_content, userFragment).commit();
        }else if (id == R.id.nav_print_member) {
            //人员查询打印
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FamilyMemberPrintFragment printFragment = FamilyMemberPrintFragment.newInstance();
            transaction.addToBackStack(null);
            transaction.replace(R.id.frame_content, printFragment).commit();
        }else if (id == R.id.nav_print_family) {
            //人员查询打印
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragmentFamilySearch familyAdd = FragmentFamilySearch.newInstance();
            transaction.addToBackStack(null);
            transaction.replace(R.id.frame_content, familyAdd).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 初始话参数
     * 用户控制版本更新问题,获取机器码并存储
     */
    private void initParams() {
        try {
            String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            PropertiesUtil mProp = PropertiesUtil.getInstance(getApplicationContext()).init();
            mProp = mProp.setFile(Constants.CONFIG_FILE);
            mProp = mProp.setPath(Constants.CONFIG_PATH);
            mProp.writeString("uuid", uuid);
            mProp.commit();
        } catch (SecurityException e) {
            LogUtil.d("获取机器码失败。。。。。。");
            return;
        }
    }

    private boolean flag = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        final MainActivity main = this;
        super.onWindowFocusChanged(hasFocus);
        if (!flag) {
            String current_uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            PropertiesUtil mProp = PropertiesUtil.getInstance(getApplicationContext()).init();

            mProp = mProp.setFile(Constants.CONFIG_FILE);
            mProp = mProp.setPath(Constants.CONFIG_PATH);
            mProp.open();
            String uuid = mProp.readString("uuid", "");
            if (!current_uuid.equals(uuid)) {
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("版权提示");
                pDialog.setContentText("请在安装系统到指定硬件设备中!");
                pDialog.setCancelable(false);
                pDialog.show();
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        main.finish();
                    }
                });
            }
            flag = true;
        }
    }
}
