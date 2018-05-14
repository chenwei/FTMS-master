package com.rds.ftms.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.tscdll.TSCActivity;
import com.kcode.bottomlib.BottomDialog;
import com.rds.ftms.MainActivity;
import com.rds.ftms.R;
import com.rds.ftms.bean.FtmsFamilyBean;
import com.rds.ftms.bean.FtmsFamilyMemberBean;
import com.rds.ftms.dao.FtmsFamilyMemberDao;
import com.rds.ftms.ui.module.SearchEditText;
import com.rds.ftms.utils.PropertiesUtil;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 家谱成员查询
 */
public class FamilyMemberPrintFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public FamilyMemberPrintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FamilyMemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FamilyMemberPrintFragment newInstance() {
        FamilyMemberPrintFragment fragment = new FamilyMemberPrintFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_FID, fid);
//        args.putString(ARG_PARAM2, pid);
//        args.putInt(ARG_LEVEL, level);
        fragment.setArguments(args);
        return fragment;
    }

    private SearchEditText search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            fid = getArguments().getString(ARG_FID);
//            pid = getArguments().getString(ARG_PARAM2);
//            level = getArguments().getInt(ARG_LEVEL)+1;
        }

    }

    private String TAG = FamilyMemberPrintFragment.class.getName();
    private ListView listView;
    private SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FamilyMemberPrintFragment memberFragment = this;
        View view = inflater.inflate(R.layout.fragment_family_member_search_print, container, false);
        listView = (ListView) view.findViewById(R.id.member_search_listview_print);
        search = (SearchEditText) view.findViewById(R.id.query);

        search.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                String content = search.getText().toString().trim();
                research(content);
            }
        });
        initItemClickEvent();
        return view;
    }

    /**
     * 获取数据
     *
     * @return
     */
    private List<? extends Map<String, ?>> queryData(String params) {
        FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(getContext());
        List<FtmsFamilyMemberBean> query_list = dao.queryByNameOrCard(params);

        List<Map<String, Object>> return_list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < query_list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            FtmsFamilyMemberBean model = query_list.get(i);
            map.put("title", model.getName());
            map.put("body", model.getIdcard());
            map.put("img", R.drawable.ic_male_avatar);
            map.put("fid", model.getFid());
            map.put("pid", model.getPid());
            map.put("id", model.getId());
            map.put("regtime", model.getRegtime());
            map.put("residence", model.getResidence().trim().length() <= 0 ? model.getAddress() : model.getResidence());
            map.put("sex", model.getSex());
            map.put("address", model.getAddress());
            return_list.add(map);
        }
        return return_list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        research();
    }

    public void research(String params) {
        adapter = new SimpleAdapter(getActivity(), queryData(params), R.layout.fragment_family_member_search_listview_print,
                new String[]{"img", "title", "body", "residence", "sex", "address", "level", "id"},
                new int[]{R.id.itemimg, R.id.itemtitle, R.id.itembody, R.id.itemresidence, R.id.itemsex, R.id.itemaddress});      //配置适配器，并获取对应Item中的ID
        listView.setAdapter(adapter);
    }

    private void initItemClickEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, Object> map = (HashMap<String, Object>) listView
                        .getItemAtPosition(i);
                String id = String.valueOf(map.get("id"));
//                Toast.makeText(getActivity(), "编辑家庭成员！"+String.valueOf(map.get("id")), Toast.LENGTH_SHORT).show();
                showDialogFragment(id);
                //Fragment 栈信息

            }
        });
    }

    public void showDialogFragment(final String id) {
        Log.i(TAG, "showDialogFragment");

        BottomDialog dialog = BottomDialog.newInstance("打印操作", "取消", new String[]{"打印采样标签"});

        /**
         *
         * BottomDialog dialog = BottomDialog.newInstance("titleText","cancelText",new String[]{"item1","item2"});
         *
         * use public static BottomDialog newInstance(String titleText,String cancelText, String[] items)
         * set cancel text
         */
        dialog.show(getChildFragmentManager(), "dialog");
        //add item click listener
        dialog.setListener(new BottomDialog.OnClickListener() {
            @Override
            public void click(int position) {
//                Toast.makeText(getContext(), "" + position, Toast.LENGTH_LONG).show();
                if (position == 0) {
                    //删除家谱信息
//                    FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(getContext());
//                    List<FtmsFamilyMemberBean> list = dao.queryById(id);
//                    if(list.size()>0) {
                    PropertiesUtil mProp = PropertiesUtil.getInstance(getContext()).init();
                    mProp.open();
                    final String company = mProp.readString("company", "");
                    final String username = mProp.readString("username", "");
                    if (company.length() <= 0 || username.length() <= 0) {
                        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("账户设置异常");
                        pDialog.setContentText("请在主菜单先设置账号!"/*+"请在主菜单栏选择【蓝牙打印机】设置"*/);
                        pDialog.setCancelable(false);
                        pDialog.show();
                        return;
                    }
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
                            String reson = text.getText().toString();
                            Date currentTime = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String nowdate = formatter.format(currentTime);

                            FtmsFamilyMemberDao dao = new FtmsFamilyMemberDao(getContext());
                            List<FtmsFamilyMemberBean> list = dao.queryMemberById(Integer.parseInt(id));
                            if (list.size() > 0) {
                                FtmsFamilyMemberBean model = list.get(0);

                                TSCActivity printUtils = MainActivity.printUtils;
                                try {
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
                                    String str = "SPEED 6\n" +
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

                                            "TEXT 30," + init_height + ",\"TSS24.BF2\",0,1,1,\"姓    名:\"\n" +
                                            "BAR 140,42,370,2\n" +
                                            "TEXT 150,15,\"TSS24.BF2\",0,1,1,\"" + model.getName() + "\"\n" +
                                            "TEXT 30,40,\"3\",0,1,1,\"\"\n" +

                                            "TEXT 30," + (init_height + step * 1) + ",\"TSS24.BF2\",0,1,1,\"性    别:\"\n" +
                                            "BAR 140," + (42 + step * 1) + ",140,2\n" +
                                            "TEXT 150," + (init_height + step * 1) + ",\"TSS24.BF2\",0,1,1,\"" + model.getSex() + "\"\n" +
                                            "TEXT 30,100,\"3\",0,1,1,\"\"\n" +

                                            "TEXT 300," + (init_height + step * 1) + ",\"TSS24.BF2\",0,1,1,\"民族:\"\n" +
                                            "BAR 370," + (42 + step * 1) + ",140,2\n" +
                                            "TEXT 380," + (init_height + step * 1) + ",\"TSS24.BF2\",0,1,1,\"" + model.getNation() + "\"\n" +

                                            "TEXT 30," + (init_height + step * 2) + ",\"TSS24.BF2\",0,1,1,\"身份证号:\"\n" +
                                            "BAR 140," + (42 + step * 2) + ",370,2\n" +
                                            "TEXT 150," + (init_height + step * 2) + ",\"TSS24.BF2\",0,1,1,\"" + model.getIdcard() + "\"\n" +
                                            "TEXT 30,150,\"3\",0,1,1,\"\"\n" +

                                            "TEXT 30," + (init_height + step * 3) + ",\"TSS24.BF2\",0,1,1,\"户    籍:\"\n" +
                                            "BAR 140," + (42 + step * 3) + ",370,2\n" +
                                            "TEXT 150," + (init_height + step * 3) + ",\"TSS24.BF2\",0,1,1,\"" + model.getAddress() + "\"\n" +
                                            "TEXT 30,200,\"3\",0,1,1,\"\"\n" +

                                            "TEXT 30," + (init_height + step * 4) + ",\"TSS24.BF2\",0,1,1,\"现 住 址:\"\n" +
                                            "BAR 140," + (42 + step * 4) + ",370,2\n" +
                                            "TEXT 150," + (init_height + step * 4) + ",\"TSS24.BF2\",0,1,1,\"" + model.getResidence() + "\"\n" +
                                            "TEXT 30,230,\"3\",0,1,1,\"\"\n" +

                                            "PRINT 1,1\n" +
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
                                            "TEXT 30," + (init_height + step * 1) + ",\"TSS24.BF2\",0,1,1,\"采集单位:\"\n" +
                                            "BAR 140," + (42 + step * 1) + ",370,2\n" +
                                            "TEXT 150," + (init_height + step * 1) + ",\"TSS24.BF2\",0,1,1,\"" + company + "\"\n" +
                                            "TEXT 30,100,\"3\",0,1,1,\"\"\n" +

                                            "TEXT 30," + (init_height + step * 2) + ",\"TSS24.BF2\",0,1,1,\"采 集 人:\"\n" +
                                            "BAR 140," + (42 + step * 2) + ",370,2\n" +
                                            "TEXT 150," + (init_height + step * 2) + ",\"TSS24.BF2\",0,1,1,\"" + username + "\"\n" +
                                            "TEXT 30,150,\"3\",0,1,1,\"\"\n" +

                                            "TEXT 30," + (init_height + step * 3) + ",\"TSS24.BF2\",0,1,1,\"采集时间:\"\n" +
                                            "BAR 140," + (42 + step * 3) + "370,2\n" +
                                            "TEXT 150," + (init_height + step * 3) + ",\"TSS24.BF2\",0,1,1,\"" + nowdate + "\"\n" +
                                            "TEXT 30,200,\"3\",0,1,1,\"\"\n" +

                                            "TEXT 30," + (init_height + step * 4) + ",\"TSS24.BF2\",0,1,1,\"采集理由:\"\n" +
                                            "BAR 140," + (42 + step * 4) + "370,2\n" +
                                            "TEXT 150," + (init_height + step * 4) + ",\"TSS24.BF2\",0,1,1,\"" + reson + "\"\n" +
                                            "PRINT 1,1\n";
                                    byte b[] = new byte[0];
                                    try {
                                        b = str.getBytes("GBK");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    printUtils.sendcommand(b);
                                    Toast.makeText(getContext(), "完成标签打印", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setTitleText("蓝牙连接异常");
                                    pDialog.setContentText("请确认打印机蓝牙连接是否正常!"/*+"请在主菜单栏选择【蓝牙打印机】设置"*/);
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                }
                            }
                        }
                    });
//                    }


                }
            }
        });
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideKeyboard(v, ev)) {
//                hideKeyboard(v.getWindowToken());
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
//    private void hideKeyboard(IBinder token) {
//        if (token != null) {
//            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }

}
