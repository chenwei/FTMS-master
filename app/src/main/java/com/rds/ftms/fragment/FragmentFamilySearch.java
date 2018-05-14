package com.rds.ftms.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kcode.bottomlib.BottomDialog;
import com.rds.ftms.R;
import com.rds.ftms.bean.FtmsFamilyBean;
import com.rds.ftms.dao.FtmsFamilyDao;
import com.rds.ftms.fragment.adapter.FtmsFamilyAdapter;
import com.rds.ftms.ui.module.SearchEditText;
import com.rds.ftms.utils.LogUtil;

import org.michaelbel.bottomsheet.BottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.widget.AbsListView.OnScrollListener;

/**
 * 家谱查询列表
 */
public class FragmentFamilySearch extends Fragment {

    private String TAG = FragmentFamilySearch.class.getName();
    private ListView listView;
    private FtmsFamilyAdapter adapter;
    private boolean is_divPage;// 是否进行分页操作
    private List<FtmsFamilyBean> oneTotal = new ArrayList<FtmsFamilyBean>();// 用来存放一页数据
    private List<FtmsFamilyBean> total = new ArrayList<FtmsFamilyBean>();// 用来存放获取的所有数据
    private ProgressDialog dialog;// 进度对话框对象
    private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
    private int pageCount;//总页数，从服务端获取过来

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FamilySearchFrament.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFamilySearch newInstance() {
        FragmentFamilySearch fragment = new FragmentFamilySearch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private SearchEditText search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family_search, container, false);
        listView = (ListView) view.findViewById(R.id.search_listview);

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("提示");
        dialog.setMessage("正在加载信息...");

        search = (SearchEditText) view.findViewById(R.id.query);
        search.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                String content = search.getText().toString().trim();
                pageNo = 1;
                total.removeAll(total);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                new FtmsFamilyTask().execute(pageNo+"",content);
//                research(content);
            }
        });


        /**
         * 用来获取数据...
         */
        new FtmsFamilyTask().execute(pageNo+"","");

        listView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /**
                 * 当分页操作is_divPage为true时、滑动停止时、且pageNo<=pageCount（这里因为服务端有pageCount页数据）时，加载更多数据。
                 */
                if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE && pageNo <= pageCount) {
                    Toast.makeText(getActivity(), "正在获取更多数据...",
                            Toast.LENGTH_SHORT).show();
                    String content = search.getText().toString().trim();
                    new FtmsFamilyTask().execute(pageNo+"",content);
                } else if (pageNo > pageCount) {
                    /**
                     * 如果pageNo>pageCount则表示，服务端没有更多的数据可供加载了。
                     */
                    Toast.makeText(getActivity(), "没有更多数据啦...",
                            Toast.LENGTH_SHORT).show();
                }
            }
            /**
             * 当第一个可见的item（firstVisibleItem）+可见的item的个数（visibleItemCount）=
             * 所有的item总数的时候， is_divPage变为TRUE，这个时候才会加载数据。
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);

            }
        });
        adapter = new FtmsFamilyAdapter(this);
        return view;
    }

    public void refresh(){
        this.pageNo = 1;
        total.removeAll(total);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        String content = search.getText().toString().trim();
        new FtmsFamilyTask().execute("" + pageNo,content);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 覆写activity的销毁方法，在销毁activity之前将初始也pageNo设置为默认值1
     * 如果没有覆写该方法并没有恢复pageNo的默认值。那么等你退出应用后，重新点击应用图标进去
     * 会获取不到任何值(因为第一次加载数据的时候，pageNo已经快速增加到pageNo的最大值)
     * 这里有什么问题或者不同的理解，请各位朋友指出。
     */
    @Override
    public void onDestroy() {
        pageNo = 1;
        super.onDestroy();
    }

    /**
     * MyTask继承线程池AsyncTask用来网络数据请求、json解析、数据更新等操作。
     * AsyncTask<Params, Progress, Result>
     * Params:在执行AsyncTask时需要传入的参数，可用于在后台任务中使用
     * Progress：后台人物执行时，如果需要在界面上显示当前的进度，则使用这里指定的泛型作为进度单位
     * Result：当人物执行完毕后，如果需要对结果进行返回，则使用这里指定的泛型作为返回值类型
     */
    class FtmsFamilyTask extends AsyncTask<String, Void, String> {
        /**
         * 数据请求前显示dialog。
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        /**
         * 在doInBackground方法中，做一些诸如网络请求等耗时操作。
         */
        @Override
        protected String doInBackground(String... params) {
//            FtmsFamilyDao dao = new FtmsFamilyDao(getActivity());
//            List<FtmsFamilyBean> reslut = dao.queryAll();
            String pageno = params[0];
            String search_param = params[1];
            FtmsFamilyDao dao = new FtmsFamilyDao(getActivity());

            return JSONObject.toJSONString(dao.queryByName(Integer.parseInt(pageno),search_param));
        }

        /**
         * 在该方法中，主要进行一些数据的处理，更新。
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                // 如果获取的result数据不为空，那么对其进行JSON解析。并显示在手机屏幕上。
                oneTotal.clear();

//                FtmsFamilyDao dao = new FtmsFamilyDao(getActivity());
//                List<FtmsFamilyBean> list = dao.queryAll();
                JSONObject object = JSONObject.parseObject(result);
                String l_result = object.get("result").toString();
                List<FtmsFamilyBean> list = JSONArray.parseArray(l_result,FtmsFamilyBean.class);
                int totalcount = Integer.parseInt(object.get("total").toString());
                pageCount = (totalcount/10)+(totalcount%10>0?1:0);

                total.addAll(list);
                adapter.bindData(total);
                /**
                 * 当pageNo等于1的时候才会setAdapter，以后不会再设置，直接notifyDataSetChanged，
                 * 进行数据更新 ，这样可避免每次加载更多数据的时候，都会重新回到第一页。
                 */
                if (pageNo == 1) {
                    listView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
                pageNo++;
                System.out.println("pageNo=" + pageNo);
            } else if (result == null) {
                Toast.makeText(getActivity(), "请求数据失败...",
                        Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
