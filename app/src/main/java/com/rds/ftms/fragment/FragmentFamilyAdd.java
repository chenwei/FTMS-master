package com.rds.ftms.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rds.ftms.R;
import com.rds.ftms.bean.FtmsFamilyBean;
import com.rds.ftms.dao.FtmsFamilyDao;
import com.rds.ftms.utils.Constants;
import com.rds.ftms.utils.LogUtil;
import com.rds.ftms.utils.PropertiesUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 家谱创建界面（新增界面）
 *
 */
public class FragmentFamilyAdd extends Fragment {

    private String TAG = FragmentFamilyAdd.class.getName();

    private boolean isupdate = false;

    private FtmsFamilyBean familyBean = null;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FamilyCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFamilyAdd newInstance() {
        FragmentFamilyAdd fragment = new FragmentFamilyAdd();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

public void setBean(FtmsFamilyBean bean){
        this.familyBean = bean;
}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    /**
     * 是否为更新数据
     */
    public void isUpdate(){
        isupdate = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_family_add, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();
        final EditText fsubdivision =  (EditText) activity.findViewById(R.id.fsubdivision);
        final EditText fmark =  (EditText) activity.findViewById(R.id.fmark);
        final EditText ffirstname =  (EditText) activity.findViewById(R.id.ffirstname);
        final EditText fname =  (EditText) activity.findViewById(R.id.fname);
        if(familyBean!=null){
            fname.setText(familyBean.getFname());
            fsubdivision.setText(familyBean.getFsubdivision());
            ffirstname.setText(familyBean.getFfirstname());
            fmark.setText(familyBean.getFremark());
        }

        //配置文件取数据
        PropertiesUtil mProp = PropertiesUtil.getInstance(getContext()).init();
        mProp.open();
        fsubdivision.setText(mProp.readString("fsubdivision",""));

        fsubdivision.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "输入文字中的状态，count是输入字符数");
                fname.setText(s.toString()+ffirstname.getText()+"的家谱");
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Log.i(TAG, "输入文本之前的状态");
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "输入文字后的状态");
            }
        });
        ffirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "输入文字中的状态，count是输入字符数");
                fname.setText(fsubdivision.getText().toString()+s.toString()+"的家谱");
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Log.i(TAG, "输入文本之前的状态");
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "输入文字后的状态");
            }
        });


        final Button button = (Button) activity.findViewById(R.id.btn_save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String t_fname = fname.getText().toString();
                String t_ffirstname = ffirstname.getText().toString();
                String t_fmark = fmark.getText().toString();
                String t_fsubdivision = fsubdivision.getText().toString();
                if(t_ffirstname.length()<=0 || t_ffirstname.length()<0||t_fsubdivision.length()<=0){
                    SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("保存异常");
                    pDialog.setContentText("确保数据填写完整!");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }else {
                    //保存数据
                    FtmsFamilyBean bean = new FtmsFamilyBean();
//                    String fid = UUID.randomUUID().toString();
//                    bean.setFid(fid);
                    bean.setFfirstname(t_ffirstname);
                    bean.setFname(t_fname);
                    bean.setFsubdivision(t_fsubdivision);
                    bean.setFremark(t_fmark);
                    bean.setFindate((String) DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()));
                    final FtmsFamilyDao dao = new FtmsFamilyDao(getContext());

                    if(isupdate){
                        try{
                            familyBean.setFid(familyBean.getFid());
                            dao.update(familyBean);
                            PropertiesUtil mProp = PropertiesUtil.getInstance(getContext()).init();
                            mProp = mProp.setFile(Constants.CONFIG_FILE);
                            mProp = mProp.setPath(Constants.CONFIG_PATH);
                            mProp.writeString("fsubdivision", t_fsubdivision);
                            mProp.commit();
                            final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("保存家系信息成功!");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }catch (SQLException e){
                            LogUtil.e("保存家谱信息失败!");
                            SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("保存异常");
                            pDialog.setContentText("保存家谱信息失败!");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }

                    }else {
                        try {
                            dao.add(bean);
                            PropertiesUtil mProp = PropertiesUtil.getInstance(getContext()).init();
                            mProp = mProp.setFile(Constants.CONFIG_FILE);
                            mProp = mProp.setPath(Constants.CONFIG_PATH);
                            mProp.writeString("fsubdivision", t_fsubdivision);
                            mProp.commit();

                            final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("保存家系信息成功，【确定】完成家系成员信息!");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    pDialog.dismiss();
                                    List<FtmsFamilyBean> beans = dao.queryAll();
                                    //跳转页面
                                    FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
//                                FamilyMemberFragment mFamilyMemberFragment = FamilyMemberFragment.newInstance(fid, "0",0);
//                                beginTransaction.addToBackStack(null);
//                                beginTransaction.replace(R.id.frame_content, mFamilyMemberFragment);
//                                beginTransaction.commit();

                                }
                            });

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
                }

            }
        });
    }


}
