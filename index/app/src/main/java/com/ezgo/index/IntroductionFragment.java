package com.ezgo.index;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class IntroductionFragment extends Fragment {

    private View view;
    private ListView listView;
    private ListAdapter listAdapter;

    public IntroductionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_introduction, container, false);

        String[] text = new String[]{
                "館區簡介", "開放時間", "參觀票價", "入園門票優待標準", "遊園列車", "寄物櫃", "娃娃車、輪椅", "遊園須知"
        };

        listView = (ListView) view.findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, text);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
                Intent intent=new Intent();
                Bundle bundle = new Bundle();

                if(position==0){
                    intent.setClass(getActivity(), AreaActivity.class);
                    startActivity(intent);
                    return;
                }else{
                    switch(position) {
                        case 1:
                            bundle.putString("fileName", "page1.html");
                            break;
                        case 2:
                            bundle.putString("fileName", "page2.html");
                            break;
                        case 3:
                            bundle.putString("fileName", "page3.html");
                            break;
                        case 4:
                            bundle.putString("fileName", "page4.html");
                            break;
                        case 5:
                            bundle.putString("fileName", "page5.html");
                            break;
                        case 6:
                            bundle.putString("fileName", "page6.html");
                            break;
                        case 7:
                            bundle.putString("fileName", "page7.html");
                            break;
                    }
                    bundle.putString("titleName","園區簡介");
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), HtmlActivity.class);
                    startActivity(intent);
                }

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
         getActivity().setTitle("園區簡介");
    }

}
