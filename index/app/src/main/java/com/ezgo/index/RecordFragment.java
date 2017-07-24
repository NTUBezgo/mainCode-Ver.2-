package com.ezgo.index;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {

    private View view;
    private SimpleAdapter adapter;
    private ListView listView;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_record, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        // 清單面版
        adapter = new SimpleAdapter(getActivity(), getData(),
                R.layout.record_content,
                new String[]{"title", "img"},
                new int[]{R.id.title,R.id.img});
        listView.setAdapter(adapter);

        return view;
    }

    private List getData() {
        ArrayList list = new ArrayList();
        Map
                map = new HashMap();map.put("title", "守護地球螢光大使");map.put("img", null); list.add(map);
                map = new HashMap();map.put("title", "猿猴一家親");map.put("img", R.drawable.medal); list.add(map);
                map = new HashMap();map.put("title", "雞年特展");map.put("img", R.drawable.medal); list.add(map);
                map = new HashMap();map.put("title", "六小福過兒童節");map.put("img", null); list.add(map);
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("歷史紀錄");
    }

}
