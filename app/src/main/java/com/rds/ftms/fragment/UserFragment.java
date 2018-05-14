package com.rds.ftms.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rds.ftms.R;
import com.rds.ftms.utils.PropertiesUtil;

/**
 * 家谱创建界面（新增界面）
 *
 */
public class UserFragment extends Fragment {

    private String TAG = UserFragment.class.getName();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FamilyCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_config, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();

        final EditText company =  (EditText) activity.findViewById(R.id.company);
        final EditText username =  (EditText) activity.findViewById(R.id.username);

        //配置文件取数据
        PropertiesUtil mProp = PropertiesUtil.getInstance(getContext()).init();
        mProp.open();
        company.setText(mProp.readString("company",""));
        username.setText(mProp.readString("username",""));

        final Button button = (Button) activity.findViewById(R.id.btn_save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String t_company = company.getText().toString();
                String t_username = username.getText().toString();
                if(t_company.length()<=0 || t_username.length()<0){
                    Toast.makeText(getActivity(), "确保数据填写完整!", Toast.LENGTH_SHORT).show();
                }else {
                    //保存数据
                    PropertiesUtil mProp = PropertiesUtil.getInstance(getContext()).init();
                    mProp.writeString("company", t_company);
                    mProp.writeString("username", t_username);
                    mProp.commit();

                }

            }
        });
    }


}
