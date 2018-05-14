package com.rds.ftms.fragment.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rds.ftms.R;
import com.rds.ftms.bean.FtmsFamilyBean;
import com.rds.ftms.dao.FtmsFamilyDao;
import com.rds.ftms.dao.FtmsFamilyMemberDao;
import com.rds.ftms.fragment.FragmentFamilyAdd;
import com.rds.ftms.fragment.FragmentFamilySearch;
import com.rds.ftms.ui.FamilyTreeActivity3;
import com.rds.ftms.ui.NoMemberActivity;
import com.rds.ftms.utils.LogUtil;

import org.michaelbel.bottomsheet.BottomSheet;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * chenwei
 * 数据库操作底层类
 */

public class FtmsFamilySearchAdapter extends BaseAdapter {


    public FtmsFamilySearchAdapter() {
    }

    private  FamilyTreeActivity3 fragment;

    public FtmsFamilySearchAdapter(FamilyTreeActivity3 fragment) {
        this.fragment = fragment;
    }

    List<FtmsFamilyBean> list;

    private int fid;
    private int pid;


    public void setFid(int fid) {
        this.fid = fid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    /**
     * bindData用来传递数据给适配器。
     *
     * @param list
     */
    public void bindData(List<FtmsFamilyBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getFid();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_family_search_listview, parent, false);

        final FtmsFamilyBean bean = list.get(position);

        ImageView iv_icon = (ImageView) view.findViewById(R.id.itemimg);
        TextView itemtitle = (TextView) view.findViewById(R.id.itemtitle);
        TextView itembody = (TextView) view.findViewById(R.id.itembody);
        iv_icon.setBackgroundResource(R.drawable.home);
        itemtitle.setText(list.get(position).getFname());
        itembody.setText(list.get(position).getFsubdivision());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c_fid = bean.getFid();

                FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(fragment);
                try{
                    dao.update(fid,pid,c_fid);
                    fragment.refresh();
                    final SweetAlertDialog pDialog = new SweetAlertDialog(fragment, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("合并家系信息成功!");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }catch (SQLException e){
                    LogUtil.e("合并家谱信息失败!");
                    SweetAlertDialog pDialog = new SweetAlertDialog(fragment, SweetAlertDialog.ERROR_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("合并家系异常");
                    pDialog.setContentText("合并家系信息失败!");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }



//                fragment.refresh();
            }
        });

        return view;
    }

    private FtmsFamilyBean getFamilyById(int fid) {
        FtmsFamilyDao dao = new FtmsFamilyDao(fragment.getApplicationContext());
        FtmsFamilyBean bean = dao.get(fid);
        return bean;
    }
}

