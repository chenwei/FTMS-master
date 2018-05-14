package com.rds.ftms.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rds.ftms.bean.FtmsFamilyMemberBean;
import com.rds.ftms.dao.FtmsFamilyMemberDao;
import com.rds.ftms.idcard.IDCardManager;
import com.rds.ftms.idcard.IDCardModel;
import com.rds.ftms.R;
import com.rds.ftms.ui.FamilyTreeActivity3;
import com.rds.ftms.utils.LogUtil;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

import cn.pda.serialport.Tools;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class FamilyMembersDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText editText_name;
    private EditText editText_sex;
    private EditText editText_nation;
    private EditText editText_year;
    private EditText editText_month;
    private EditText editText_day;
    private EditText editText_address;
    private EditText editText_IDCard;
    private EditText editText_office;
    private EditText editText_effective;
    private EditText editText_residence;

    private Button button_open;
    private Button button_clear;
    private Button button_close;
    private Button button_finish;
    private Button button_save;
    private ImageView imageView;
    private Bitmap photoBitmap = null;
    private IDCardManager manager;
    private ReadThread thread;
    private Toast toast;

    private static final String ARG_FID = "fid";
    private static final String ARG_PID = "pid";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param p_fid Parameter 1.
     * @param p_pid Parameter 2.
     * @return A new instance of fragment FamilyMemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FamilyMembersDialogFragment newInstance(int p_fid, int p_pid, Context context) {
        FamilyMembersDialogFragment fragment = new FamilyMembersDialogFragment(p_fid, p_pid, context);
        Bundle args = new Bundle();
        args.putInt(ARG_FID, p_fid);
        args.putInt(ARG_PID, p_pid);
        fragment.setArguments(args);
        return fragment;
    }

    private int fid;
    private int pid;

    public FamilyMembersDialogFragment() {

    }

    private Context context;

    @SuppressLint("ValidFragment")
    public FamilyMembersDialogFragment(int p_fid, int p_pid, Context context) {
        this.fid = p_fid;
        this.pid = p_pid;
        this.context = context;
    }

    private void showToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(this.getContext(), info, 0);
        } else {
            toast.setText(info);
        }
        toast.show();
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showToast("读身份证成功！");
                    Bundle bundle = msg.getData();
                    //获取身份证信息：姓名、性别、出生年、月、日、住址、身份证号、签发机关、有效期开始、结束、（额外信息新地址（一般情况为空））
                    String name = bundle.getString("name");
                    String sex = bundle.getString("sex");
                    String nation = bundle.getString("nation");
                    String year = bundle.getString("year");
                    String month = bundle.getString("month");
                    String day = bundle.getString("day");
                    String address = bundle.getString("address");
                    String id = bundle.getString("id");
                    String office = bundle.getString("office");
                    String start = bundle.getString("begin");
                    String stop = bundle.getString("end");
                    String newaddress = bundle.getString("newaddress");
                    String fp1 = bundle.getString("fp1");
                    String fp2 = bundle.getString("fp2");
                    //获取图片位图，并显示：
                    imageView.setImageBitmap(photoBitmap);
                    editText_name.setText(name);
                    editText_sex.setText(sex);
                    editText_nation.setText(nation);
                    editText_year.setText(year);
                    editText_month.setText(month);
                    editText_day.setText(day);
                    editText_address.setText(address);
                    editText_IDCard.setText(id);
                    editText_office.setText(office);
                    editText_effective.setText(start + "-" + stop);
                    break;
                case 1:
                    clear();
                    showToast("发现身份证!\n正在获取身份证数据...");
                    break;
                case 2:
                    showToast("");
                    break;
                case 3:

                    break;
                default:
                    break;
            }
        }

        ;
    };

    private FamilyTreeActivity3 familyTreeActivity3;
    public void setFamilyTreeActivity3(FamilyTreeActivity3 familyTreeActivity3){
        this.familyTreeActivity3 = familyTreeActivity3;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_family_member_form, null);
        // Inflate and set the layout for the dialog
        View view = inflater.inflate(R.layout.vertical, null);
        // Pass null as the parent view because its going in the dialog layout
        initView(view);
        String path = Environment.getExternalStorageDirectory() + "/IDCard";
        File file_paper = new File(path);
        if (!file_paper.exists()) {
            file_paper.mkdirs();
        }
        thread = new ReadThread();
        thread.start();
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FtmsFamilyMemberBean model = new FtmsFamilyMemberBean();
                        model.setAddress(editText_address.getText().toString());
                        model.setBirthday(editText_year.getText().toString() + "-" + editText_month.getText().toString() + "-" + editText_day.getText().toString());
                        model.setEffective(editText_effective.getText().toString());
                        model.setFid(fid);
                        model.setIdcard(editText_IDCard.getText().toString());
                        model.setName(editText_name.getText().toString());
                        model.setNation(editText_nation.getText().toString());
                        model.setOffice(editText_office.getText().toString());
                        model.setPid(pid);
                        model.setRegtime((String) DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()));
                        model.setResidence(editText_residence.getText().toString());
                        model.setSex(editText_sex.getText().toString());
                        FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(getContext());
                        try {
                            dao.add(model);
                            final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("保存家系信息成功!");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            if (pid == 0) {
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                        Intent startIntent = new Intent(context, FamilyTreeActivity3.class);
                                        Bundle argBundle = new Bundle();
                                        argBundle.putInt("fid", fid);
                                        startIntent.putExtras(argBundle);
                                        context.startActivity(startIntent);
                                    }
                                });
                            }
                            if(familyTreeActivity3!=null){
                                familyTreeActivity3.refresh();
                            }
                        } catch (SQLException e) {
                            LogUtil.e("保存家谱信息失败!");
                            SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("保存异常");
                            pDialog.setContentText("保存家谱信息失败!");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                showToast("cancel");
            }
        });

        //打开读卡器
        if (manager == null) {
            manager = new IDCardManager(getContext());
        }
        startFlag = true;

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startFlag = false;
        if (manager != null) {
            manager.close();
        }
        runFlag = false;
        try {
//			clearWallpaper();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.toString();
        }
    }

    //app暂停
    private boolean pasue = false;

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!pasue && manager != null) {
            pasue = true;
            manager.close();
            manager = null;
            startFlag = false;
        }
    }

    //app暂停后重启
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (pasue) {
            pasue = false;
            if (manager == null) {
                manager = new IDCardManager(getContext());
            }
            startFlag = true;
        }
    }

    private void initView(View view) {
        // TODO Auto-generated method stub
        editText_name = (EditText) view.findViewById(R.id.editText_name);
        editText_sex = (EditText) view.findViewById(R.id.editText_sex);
        editText_nation = (EditText) view.findViewById(R.id.editText_nation);
        editText_year = (EditText) view.findViewById(R.id.editText_year);
        editText_month = (EditText) view.findViewById(R.id.editText_month);
        editText_day = (EditText) view.findViewById(R.id.editText_day);
        editText_address = (EditText) view.findViewById(R.id.editText_address);
        editText_IDCard = (EditText) view.findViewById(R.id.editText_IDCard);
        editText_office = (EditText) view.findViewById(R.id.editText_office);
        editText_effective = (EditText) view.findViewById(R.id.editText_effective);
        editText_residence = (EditText) view.findViewById(R.id.editText_residence);

//		editText_info = (EditText) view.findViewById(R.id.editText_info);
        button_open = (Button) view.findViewById(R.id.button_open);
        button_clear = (Button) view.findViewById(R.id.button_clear);
        button_close = (Button) view.findViewById(R.id.button_close);
        button_finish = (Button) view.findViewById(R.id.button_finish);

        button_save = (Button) view.findViewById(R.id.button_save);

        button_close.setOnClickListener(this);
        button_finish.setOnClickListener(this);
        button_open.setOnClickListener(this);
        button_clear.setOnClickListener(this);
        button_save.setOnClickListener(this);
        OpenOrCloseView(true);
        imageView = (ImageView) view.findViewById(R.id.imageView);
    }

    private void OpenOrCloseView(boolean flag) {
        if (flag) {
            button_close.setClickable(false);
//		button_finish.setClickable(false);
            button_clear.setClickable(false);
            button_clear.setTextColor(Color.GRAY);
//		button_finish.setTextColor(Color.GRAY);
            button_close.setTextColor(Color.GRAY);
            button_open.setClickable(true);
            button_open.setTextColor(Color.BLACK);
        } else {
            button_close.setClickable(true);
            button_finish.setClickable(true);
            button_clear.setClickable(true);
            button_clear.setTextColor(Color.BLACK);
            button_finish.setTextColor(Color.BLACK);
            button_close.setTextColor(Color.BLACK);
            button_open.setClickable(false);
            button_open.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_open:
                OpenOrCloseView(false);
                if (manager == null) {
                    manager = new IDCardManager(getContext());
                }
                startFlag = true;
                break;
            case R.id.button_clear:
                clear();
                break;
            case R.id.button_close:
                startFlag = false;
                OpenOrCloseView(true);

                if (manager != null) {
                    manager.close();
                    manager = null;
                }
                break;
            case R.id.button_finish:
                runFlag = false;
                ;
//                finish();
                break;
            case R.id.button_save:

                break;
            default:
                break;
        }
    }

    private void clear() {
        editText_name.setText("");
        editText_sex.setText("");
        editText_nation.setText("");
        editText_year.setText("");
        editText_month.setText("");
        editText_day.setText("");
        editText_address.setText("");
        editText_IDCard.setText("");
        editText_office.setText("");
        editText_effective.setText("");
        imageView.setImageResource(R.drawable.photo);
    }

    private boolean runFlag = true;
    private boolean startFlag = false;

    private class ReadThread extends Thread {
        @Override
        public void run() {
            while (runFlag) {
                if (startFlag && manager != null) {
                    if (manager.findCard(200)) {
                        handler.sendEmptyMessage(1);
                        IDCardModel model = null;

//                        long time = System.currentTimeMillis();
//                        if (checkBox_fp.isChecked()) {
                        //获取身份证信息、图像、指纹
//                            model = manager.getDataFP(2000);
//                            Log.e("get data time:", System.currentTimeMillis() - time +"ms");
//                        }

                        if (model != null) {
                            sendMessage(model.getName(), model.getSex(), model.getNation(),
                                    model.getYear(), model.getMonth(), model.getDay(),
                                    model.getAddress(), model.getIDCardNumber(), model.getOffice(),
                                    model.getBeginTime(), model.getEndTime(), model.getOtherData(),
                                    model.getPhotoBitmap(), Tools.Bytes2HexString(model.getFP1(), 512), Tools.Bytes2HexString(model.getFP2(), 512));
                        } else {
                            //获取身份证信息、图像
                            model = manager.getData(2000);
                            if (model != null) {
                                sendMessage(model.getName(), model.getSex(), model.getNation(),
                                        model.getYear(), model.getMonth(), model.getDay(),
                                        model.getAddress(), model.getIDCardNumber(), model.getOffice(),
                                        model.getBeginTime(), model.getEndTime(), model.getOtherData(),
                                        model.getPhotoBitmap(), "未读出指纹数据", "未读出指纹数据");
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            super.run();
        }

        private void sendMessage(String name, String sex, String nation,
                                 String year, String month, String day, String address, String id,
                                 String office, String start, String stop, String newaddress
                , Bitmap bitmap, String fp1, String fp2) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("sex", sex);
            bundle.putString("nation", nation);
            bundle.putString("year", year);
            bundle.putString("month", month);
            bundle.putString("day", day);
            bundle.putString("address", address);
            bundle.putString("id", id);
            bundle.putString("office", office);
            bundle.putString("begin", start);
            bundle.putString("end", stop);
            bundle.putString("newaddress", newaddress);
            bundle.putString("fp1", fp1);
            bundle.putString("fp2", fp2);
            photoBitmap = bitmap;
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            Toast.makeText(MainActivity.this, "Version:"+getVersion(), Toast.LENGTH_LONG).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    public String getVersion() {
//        try {
//            PackageManager manager = this.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
//            String version = info.versionName;
//            return "" + version;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
}