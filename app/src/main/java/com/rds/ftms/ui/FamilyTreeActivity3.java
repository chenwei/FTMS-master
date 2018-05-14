package com.rds.ftms.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.rds.ftms.R;
import com.rds.ftms.bean.FtmsFamilyMemberBean;
import com.rds.ftms.dao.FtmsFamilyMemberDao;
import com.rds.ftms.fragment.FamilyMembersDialogFragment;
import com.rds.ftms.interfaces.OnFamilyClickListener;
import com.rds.ftms.interfaces.module.FamilyBean;
import com.rds.ftms.ui.dialog.BottomDialog;
import com.rds.ftms.ui.view.FamilyTreeView3;
import com.rds.ftms.utils.AssetsUtil;

import java.util.List;
import java.util.Map;

/**
 * 仿亲友+
 */

public class FamilyTreeActivity3 extends BaseActivity {

    private static String MY_ID = "3ed84baa-f185-427d-b407-23acbd25a9bc";

    private FamilyTreeView3 ftvTree;//没有养父母

    //家谱ID
    private int fid;

    private List<Map<String, Object>> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_tree3);

        fid = getIntent().getIntExtra("fid",0);

        FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(getBaseContext());
        result = dao.queryByFamily(fid);

        initView();


        String appName = getString(R.string.app_name);
        permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        refuseTips = new String[]{
                String.format("在设置-应用-%1$s-权限中开启存储权限，以正常使用该功能", appName),
        };
        setData();
        setPermissions();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ftvTree != null)
            ftvTree.destroyView();
    }

    @Override
    public void onPermissionSuccess() {
        setData();
    }

    private void initView() {
        ftvTree = (FamilyTreeView3) findViewById(R.id.ftv_tree);
    }

    private void setData() {
//        String json = AssetsUtil.getAssetsTxtByName(this, "family_tree.txt");
        if(result.size()>0){
            MY_ID = (result.get(0)).get("memberId").toString();
            String res = JSONObject.toJSONString(result);
            System.out.println(MY_ID);
            System.out.println(res);
            List<FamilyBean> mList = JSONObject.parseArray(res, FamilyBean.class);

            ftvTree.saveData(mList);
            ftvTree.drawFamilyTree(MY_ID);
            ftvTree.setOnFamilyClickListener(familyClick);
        }else{
            Toast.makeText(getApplicationContext(),"家谱中没有添加人员信息",0);
        }
    }

    public void refresh(){
        FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(getBaseContext());
        result = dao.queryByFamily(fid);

        setData();
    }

    private OnFamilyClickListener familyClick = new OnFamilyClickListener() {
        @Override
        public void onFamilySelect(FamilyBean family) {
//            if (family.isSelect()) {
//                ToastMaster.toast(family.getMemberName());
//            } else {
            //获取人员ID
            int currentFamilyId = Integer.parseInt(family.getMemberId());
//                System.out.println("sdfsdf");
//                ftvTree.drawFamilyTree(currentFamilyId+"");
            showBottomMenus(currentFamilyId);
//            }
        }
    };

    private void showBottomMenus(int memberid) {

        final BottomDialog bottomDialog = new BottomDialog(this, R.style.BottomDialog);
        bottomDialog.setMemberid(memberid);
        bottomDialog.setFid(fid);

        bottomDialog.show();
    }

}
