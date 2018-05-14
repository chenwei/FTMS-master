package com.rds.ftms.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tscdll.TSCActivity;
import com.rds.ftms.MainActivity;
import com.rds.ftms.R;
import com.rds.ftms.bluetoothprinter.DeviceListActivity;
import com.rds.ftms.dao.FtmsFamilyMemberDao;
import com.rds.ftms.bean.FtmsFamilyMemberBean;
import com.rds.ftms.fragment.FamilyMembersDialogFragment;
import com.rds.ftms.fragment.FamilySearchDialogFragment;
import com.rds.ftms.ui.FamilySearchActivity;
import com.rds.ftms.ui.FamilyTreeActivity3;
import com.rds.ftms.utils.DensityUtil;
import com.rds.ftms.utils.LogUtil;
import com.rds.ftms.utils.PropertiesUtil;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 默认V7 AlertDialog
 */

public class BottomDialog extends Dialog{
    private FamilyTreeActivity3 context;
    private TextView txt;
    private Button   btnok,btnedit,btncancle,btnsave;
    private OnClickListener mListener;

    private int memberid;

    public interface LeaveMyDialogListener{
        public void onClick(View view);
    }

    public BottomDialog(FamilyTreeActivity3 context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public BottomDialog(FamilyTreeActivity3 context, int theme) {
        super(context,theme);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public void setMemberid(int memberid){
        this.memberid = memberid;
    }

    public void setFid(int fid){
        this.fid = fid;
    }

    private int fid;
    public void setListener(OnClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final BottomDialog dialog = this;
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        final View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_content_circle, null);
        this.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(context, 16f);
        params.bottomMargin = DensityUtil.dp2px(context, 8f);
        contentView.setLayoutParams(params);
        this.setCanceledOnTouchOutside(true);
        this.getWindow().setGravity(Gravity.BOTTOM);
        this.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);


        TextView tvPrint= (TextView) contentView.findViewById(R.id.print_label);
        TextView previous= (TextView) contentView.findViewById(R.id.previous);
        TextView next= (TextView) contentView.findViewById(R.id.next);
        TextView tvCancel= (TextView) contentView.findViewById(R.id.cancel);

        TextView hebing= (TextView) contentView.findViewById(R.id.hebing);
        TextView btn_delete= (TextView) contentView.findViewById(R.id.btn_delete);


        /**
         * 上一代
         *
         */
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                FamilyMembersDialogFragment fragment = FamilyMembersDialogFragment.newInstance(fid,0,context);
//                FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                fragment.show(ft, "第一代");

            }
        });

        /**
         * 上一代
         *
         */
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(context);
                FtmsFamilyMemberBean bean = new FtmsFamilyMemberBean();
                bean.setId(memberid);

                try{
                    dao.delete(bean);
                    context.refresh();
                    final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("删除家系成员信息成功!");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }catch (SQLException e){
                    LogUtil.e("删除家系成员信息失败!");
                    SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("删除家系成员异常");
                    pDialog.setContentText("删除家系成员信息失败!");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FamilyMembersDialogFragment fragment = FamilyMembersDialogFragment.newInstance(fid,memberid,context);
                FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragment.show(ft, "第一代");
                fragment.setFamilyTreeActivity3(context);

            }
        });

        /**
         * 家谱合并操作
         */
        hebing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FamilySearchDialogFragment fragment = FamilySearchDialogFragment.newInstance(fid,memberid,context);
                FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragment.show(ft, "第一代");

//                Intent intent = new Intent(context.getApplication(),
//                        FamilySearchActivity.class);
//                context.startActivityForResult(intent, 101);

            }
        });

        tvPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(getContext());
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("输入采样理由");
                pDialog.setCancelable(true);
                pDialog.show();
                final EditText text = new EditText(getContext());
                pDialog.setCustomView(text);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pDialog.dismiss();

                        //配置文件取数据
                        PropertiesUtil mProp = PropertiesUtil.getInstance(getContext()).init();
                        mProp.open();
                        String company = mProp.readString("company","");
                        String username = mProp.readString("username","");
                        if(company.length()<=0||username.length()<=0){
                            SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("账户设置异常");
                            pDialog.setContentText("请在主菜单先设置账号!"/*+"请在主菜单栏选择【蓝牙打印机】设置"*/);
                            pDialog.setCancelable(false);
                            pDialog.show();
                            return;
                        }
                        String reson = text.getText().toString();
                        Date currentTime = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String nowdate = formatter.format(currentTime);

                        FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(getContext());
                        List<FtmsFamilyMemberBean> list =  dao.queryMemberById(memberid);
                        if(list.size()>0) {
                            FtmsFamilyMemberBean model = list.get(0);

                            TSCActivity printUtils = MainActivity.printUtils;
                            try{
                                //GapSet();            这个函数自己设置浓度 速度  纸张那些  不过要把  下面打印机设置注释掉
                                printUtils.clearbuffer();
                                /**
                                 * 打印机设置
                                 */
                                // printUtils.sendcommand( "SPEED 3\n"+"DENSITY 6\n" );  //设置速度和浓度

                                //printUtils.sendcommand("DIRECTION 1\n"); //打印方向 参数有0  1

                                // printUtils.sendcommand("SIZE 100.00 mm,150.00 mm\n");//这是纸张的大小  宽高

                                printUtils.sendcommand("GAP 3.00 mm,0.00 mm\n"); //两个标签纸的间距 只需填第一个参数

                                // printUtils.sendcommand("OFFSET 0.00 mm\n"+/*纸张偏移*/ "CLS\n"+/*清除上次打印缓存*/"REFERENCE 0,0\n" /*原点*/);

                                /**
                                 * 打印内容以下        BARCODE为条形码         TEXT为打印文字
                                 *                     BAR为打印直线          BOX为打印长方形
                                 */
                                //String str = "可以改变str的参数\n";
                                //printUtils.sendcommand("QRCODE 10,10,L,6,A,0,M1,S3,"+"\""+str+"\""+"\n");
                                int step = 45;
                                int init_height = 15;
                                int init_width = 30;
                                String str="SPEED 6\n" +
                                        "DENSITY 8\n" +
                                        "SET PEEL OFF\n" +
                                        "SET CUTTER OFF\n" +
                                        "SET PARTIAL_CUTTER OFF\n" +
                                        "SET TEAR ON\n" +
                                        "DIRECTION 1\n" +
                                        "SIZE 70.00 mm,30.00 mm\n" +
                                        "GAP 3 mm,0 mm\n" +
                                        "OFFSET 1.00 mm\n" +
                                        "SHIFT 0\n" +
                                            "REFERENCE 0,0\n" +
                                        "CLS\n" +

                                        "TEXT 30,"+init_height+",\"TSS24.BF2\",0,1,1,\"姓    名:\"\n" +
                                        "BAR 140,42,370,2\n" +
                                        "TEXT 150,15,\"TSS24.BF2\",0,1,1,\""+model.getName()+"\"\n" +
                                        "TEXT 30,40,\"3\",0,1,1,\"\"\n" +

                                        "TEXT 30,"+(init_height+step*1)+",\"TSS24.BF2\",0,1,1,\"性    别:\"\n" +
                                        "BAR 140,"+(42+step*1)+",140,2\n" +
                                        "TEXT 150,"+(init_height+step*1)+",\"TSS24.BF2\",0,1,1,\""+model.getSex()+"\"\n" +
                                        "TEXT 30,100,\"3\",0,1,1,\"\"\n" +

                                        "TEXT 300,"+(init_height+step*1)+",\"TSS24.BF2\",0,1,1,\"民族:\"\n" +
                                        "BAR 370,"+(42+step*1)+",140,2\n" +
                                        "TEXT 380,"+(init_height+step*1)+",\"TSS24.BF2\",0,1,1,\""+model.getNation()+"\"\n" +

                                        "TEXT 30,"+(init_height+step*2)+",\"TSS24.BF2\",0,1,1,\"身份证号:\"\n" +
                                        "BAR 140,"+(42+step*2)+",370,2\n" +
                                        "TEXT 150,"+(init_height+step*2)+",\"TSS24.BF2\",0,1,1,\""+model.getIdcard()+"\"\n" +
                                        "TEXT 30,150,\"3\",0,1,1,\"\"\n" +

                                        "TEXT 30,"+(init_height+step*3)+",\"TSS24.BF2\",0,1,1,\"户    籍:\"\n" +
                                        "BAR 140,"+(42+step*3)+",370,2\n" +
                                        "TEXT 150,"+(init_height+step*3)+",\"TSS24.BF2\",0,1,1,\""+model.getAddress()+"\"\n" +
                                        "TEXT 30,200,\"3\",0,1,1,\"\"\n" +

                                        "TEXT 30,"+(init_height+step*4)+",\"TSS24.BF2\",0,1,1,\"现 住 址:\"\n" +
                                        "BAR 140,"+(42+step*4)+",370,2\n" +
                                        "TEXT 150,"+(init_height+step*4)+",\"TSS24.BF2\",0,1,1,\""+model.getResidence()+"\"\n" +
                                        "TEXT 30,230,\"3\",0,1,1,\"\"\n" +

                                        "PRINT 1,1\n"+
                                        "SPEED 6\n" +
                                        "DENSITY 8\n" +
                                        "SET PEEL OFF\n" +
                                        "SET CUTTER OFF\n" +
                                        "SET PARTIAL_CUTTER OFF\n" +
                                        "SET TEAR ON\n" +
                                        "DIRECTION 1\n" +
                                        "SIZE 70.00 mm,30.00 mm\n" +
                                        "GAP  mm,0 mm\n" +
                                        "OFFSET 1.00 mm\n" +
                                        "SHIFT 0\n" +
                                        "REFERENCE 0,0\n" +
                                        "CLS\n" +
                                        "TEXT 30,"+(init_height+step*1)+",\"TSS24.BF2\",0,1,1,\"采集单位:\"\n" +
                                        "BAR 140,"+(42+step*1)+",370,2\n" +
                                        "TEXT 150,"+(init_height+step*1)+",\"TSS24.BF2\",0,1,1,\""+company+"\"\n" +
                                        "TEXT 30,100,\"3\",0,1,1,\"\"\n" +

                                        "TEXT 30,"+(init_height+step*2)+",\"TSS24.BF2\",0,1,1,\"采 集 人:\"\n" +
                                        "BAR 140,"+(42+step*2)+",370,2\n" +
                                        "TEXT 150,"+(init_height+step*2)+",\"TSS24.BF2\",0,1,1,\""+username+"\"\n" +
                                        "TEXT 30,150,\"3\",0,1,1,\"\"\n" +

                                        "TEXT 30,"+(init_height+step*3)+",\"TSS24.BF2\",0,1,1,\"采集时间:\"\n" +
                                        "BAR 140,"+(42+step*3)+"370,2\n" +
                                        "TEXT 150,"+(init_height+step*3)+",\"TSS24.BF2\",0,1,1,\""+nowdate+"\"\n" +
                                        "TEXT 30,200,\"3\",0,1,1,\"\"\n" +

                                        "TEXT 30,"+(init_height+step*4)+",\"TSS24.BF2\",0,1,1,\"采集理由:\"\n" +
                                        "BAR 140,"+(42+step*4)+"370,2\n" +
                                        "TEXT 150,"+(init_height+step*4)+",\"TSS24.BF2\",0,1,1,\""+reson+"\"\n" +
                                        "PRINT 1,1\n";
                                byte b[] = new byte[0];
                                try {
                                    b = str.getBytes("GBK");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                printUtils.sendcommand(b);
                                Toast.makeText(getContext(), "完成标签打印", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("蓝牙连接异常");
                                pDialog.setContentText("请确认打印机蓝牙连接是否正常!"/*+"请在主菜单栏选择【蓝牙打印机】设置"*/);
                                pDialog.setCancelable(false);
                                pDialog.show();
                            }
                                dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                });

//                new  AlertDialog.Builder(getContext())
//                        .setTitle("" )
//                        .setIcon(android.R.drawable.ic_dialog_info)
//                        .setView(new EditText(getContext()))
//                        .setPositiveButton("确定" , null)
//                        .setNegativeButton("取消" ,  null )
//                        .show();

//
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"拍照canel",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    public void dismiss() {
        BottomDialog.super.dismiss();
    }
}
