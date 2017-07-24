package com.ezgo.index;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class NavigationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_navigation);

        //預設字型
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/wp010-08.ttf");

        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 1000); //1秒跳轉
    }

    //-------------------------------------------------------------------------------------------------
    private static final int GOTO_MAIN_ACTIVITY = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    Intent intent = new Intent();
                    //將原本Activity的換成MainActivity
                    intent.setClass(NavigationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        }

    };
    //-------------------------------------------------------------------------------------------------
}
