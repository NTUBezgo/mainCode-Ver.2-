package com.ezgo.index;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by 8320E on 2017/5/26.
 */

public class AlertQuestion {

    private String[] province = new String[] { "喝媽媽奶水長大的囉", "爸媽消化過後的糜狀魚", "海中的浮游生物"};
    Context context;

    public AlertQuestion(Context con){
        context=con;
    }

    public void showSingleChoiceButton()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("想想看,企鵝寶寶吃什麼？");
        builder.setSingleChoiceItems(province, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener()
        { //設定確定按鈕
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(context,"恭喜你答對囉!",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
