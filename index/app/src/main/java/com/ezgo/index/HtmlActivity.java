package com.ezgo.index;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class HtmlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //=============================================================================
    // 首次載入App時會執行onResume(), 下次Activity由背景回到前景時也會執行onResume()
    //=============================================================================
    @Override
    protected void onResume() {
        super.onResume();
        //-------------------------
        // 取得傳來Bundle中的參數
        //-------------------------
        Bundle bundle=getIntent().getExtras();
        String fileName=bundle.getString("fileName");   //取得網頁名稱
        String titleName=bundle.getString("titleName"); //取得標題名稱

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(titleName);

        //-------------------------
        // 顯示網頁內容
        //-------------------------
        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //----------------------------------------------------------
        // 載入存在<assets>中的檔案, 注意:android_asset後沒有"s"
        //----------------------------------------------------------
        webView.loadUrl("file:///android_asset/html/" + fileName);
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
