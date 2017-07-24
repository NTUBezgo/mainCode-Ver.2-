package com.ezgo.index;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.actions.ItemListIntents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkSheetFragment extends Fragment {

    private View view;
    private SimpleAdapter adapter;
    private ListView listView;

    private MyData myData = new MyData();

    private FragmentManager fragmentManager = getFragmentManager();
    private FragmentTransaction fragmentTransaction;

    public WorkSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_worksheet, container, false);



        listView = (ListView) view.findViewById(R.id.W_listView);

        // 清單面版
        adapter = new SimpleAdapter(getActivity(), getData(),
                R.layout.worksheet_content,
                new String[]{"title", "img"},
                new int[]{R.id.title,R.id.img});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
                if (position==0){

                }else if(position==1){

                }

                Toast.makeText(getActivity(), "請至此動物區尋找題目與答案", Toast.LENGTH_SHORT).show();
                myData.fromWS(position);
                ((MainActivity)getActivity()).jumpToMainFragment();
            }
        });

        return view;
    }

    private List getData() {
        ArrayList list = new ArrayList();
        Map
        map = new HashMap();map.put("title", "想想看,企鵝寶寶吃甚麼?");map.put("img", R.drawable.lock); list.add(map);
        map = new HashMap();map.put("title", "你知道猿猴的便便跟黑猩猩的便便看起來有甚麼差別嗎?");map.put("img", R.drawable.check); list.add(map);
        map = new HashMap();map.put("title", "哪個是黑熊的腳印?");map.put("img", R.drawable.lock); list.add(map);
        map = new HashMap();map.put("title", "成年熊貓一天睡幾個小時?");map.put("img", R.drawable.lock); list.add(map);

        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("本期學習單");
    }

}
