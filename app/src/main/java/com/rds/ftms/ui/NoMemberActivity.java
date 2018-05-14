package com.rds.ftms.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rds.ftms.R;
import com.rds.ftms.dao.FtmsFamilyDao;
import com.rds.ftms.dao.FtmsFamilyMemberDao;
import com.rds.ftms.fragment.FamilyMembersDialogFragment;
import com.rds.ftms.fragment.FragmentFamilyAdd;
import com.rds.ftms.fragment.FragmentFamilySearch;
import com.rds.ftms.utils.Constants;
import com.rds.ftms.utils.LogUtil;
import com.rds.ftms.utils.PropertiesUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class NoMemberActivity extends AppCompatActivity {

    //家谱ID
    private int fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fid = getIntent().getIntExtra("fid",0);
        setContentView(R.layout.no_member_layout);
        Button button = (Button) findViewById(R.id.btn_nomember);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                FamilyMembersDialogFragment fragment = FamilyMembersDialogFragment.newInstance(fid,0,getApplicationContext());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragment.show(ft, "第一代");

            }
        });
    }

}
