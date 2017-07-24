package com.ezgo.index;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AreaActivity extends AppCompatActivity {

    private List list=null;
    private List groupkey=new ArrayList();
    private List aList = new ArrayList();
    private List bList = new ArrayList();
    private List cList = new ArrayList();
    private ListView listview;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        listview=(ListView) findViewById(R.id.listView_list);
        initData();
        MyAdapter adapter=new MyAdapter();
        listview.setAdapter(adapter);

        context=this;

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("館區簡介");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Bundle bundle = new Bundle();
                Intent intent = new Intent();

                switch(position) {
                    case 1:
                        bundle.putString("fileName", "page0_01.html");
                        break;
                    case 2:
                        bundle.putString("fileName", "page0_02.html");
                        break;
                    case 3:
                        bundle.putString("fileName", "page0_03.html");
                        break;
                    case 4:
                        bundle.putString("fileName", "page0_04.html");
                        break;
                    case 5:
                        bundle.putString("fileName", "page0_05.html");
                        break;
                    case 6:
                        bundle.putString("fileName", "page0_06.html");
                        break;
                    case 8:
                        bundle.putString("fileName", "page0_11.html");
                        break;
                    case 9:
                        bundle.putString("fileName", "page0_12.html");
                        break;
                    case 10:
                        bundle.putString("fileName", "page0_13.html");
                        break;
                    case 11:
                        bundle.putString("fileName", "page0_14.html");
                        break;
                    case 12:
                        bundle.putString("fileName", "page0_15.html");
                        break;
                    case 13:
                        bundle.putString("fileName", "page0_16.html");
                        break;
                    case 14:
                        bundle.putString("fileName", "page0_17.html");
                        break;
                    case 15:
                        bundle.putString("fileName", "page0_18.html");
                        break;
                    case 17:
                        bundle.putString("fileName", "page0_21.html");
                        break;
                    case 18:
                        bundle.putString("fileName", "page0_22.html");
                        break;
                    case 19:
                        bundle.putString("fileName", "page0_23.html");
                        break;
                }
                bundle.putString("titleName","館區簡介");
                intent.putExtras(bundle);
                intent.setClass(context, HtmlActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initData(){
        list = new ArrayList();

        groupkey.add("室內區");
        groupkey.add("戶外區");
        groupkey.add("特展區");

        aList.add("教育中心");
        aList.add("企鵝館");
        aList.add("無尾熊館");
        aList.add("兩棲爬蟲動物館");
        aList.add("昆蟲館");
        aList.add("大熊貓館");
        list.add("室內區");
        list.addAll(aList);

        bList.add("臺灣動物區");
        bList.add("兒童動物區");
        bList.add("亞洲動物區");
        bList.add("沙漠動物區");
        bList.add("澳洲動物區");
        bList.add("非洲動物區");
        bList.add("溫帶動物區");
        bList.add("鳥園區");
        list.add("戶外區");
        list.addAll(bList);

        cList.add("酷Cool節能屋");
        cList.add("高氏宗祠文史館");
        cList.add("生命驛站");
        list.add("特展區");
        list.addAll(cList);
    }

    private class MyAdapter extends BaseAdapter {

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
            return position;
        }
        @Override
        public boolean isEnabled(int position) {
            if(groupkey.contains(getItem(position))){
                return false;
            }
            return super.isEnabled(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=convertView;
            if(groupkey.contains(getItem(position))){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.area_title, null);
            }else{
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.area_content, null);
            }
            TextView text=(TextView) view.findViewById(R.id.addexam_list_item_text);
            text.setText((CharSequence) getItem(position));
            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
