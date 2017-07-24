package com.ezgo.index;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 8320E on 2017/4/5.
 */

public class MyData {

    //建立各館區marker
    private String areaMarkers[][]={
            {"24.9985962","121.5805931","臺灣動物區"},{"24.9989718","121.5819383","兒童動物區"},
            {"24.9950215","121.5834188","亞洲熱帶雨林區"},{"24.9952621"," 121.5851489  ","沙漠動物區"},
            {"24.994184","121.5853326","澳洲動物區"},{"24.9951333","121.5880094","非洲動物區"},
            {"24.9931447","121.5896013","溫帶動物區"},{"24.9957179","121.5888946","鳥園區"},
            {"24.9929758","121.5911959","企鵝館"},{"24.9982291","121.5828744","無尾熊館"},
            {"24.9940697","121.5898494","兩棲爬蟲動物館"},{"24.9967402","121.5807004","昆蟲館"},
            {"24.9968265","121.5830956","大貓熊館"},
    };

    //建立學習單marker
    private String workSheetMarkers[][]={
            {"24.9946021","121.5831828","白手長臂猿"},{"24.9983738","121.5823688","無尾熊"},
            {"24.9981684","121.5805918","梅花鹿"},{"24.9949741","121.5855928","非洲野驢"},
            {"24.992278","121.5892527","小爪水獺"},{"24.9928761","121.5910631","黑腳企鵝"}
    };

    //建立地理圍欄範圍
    private Double geofenceList[][]={
            {25.00234, 121.48368, 1.0}, //測試地
            {25.043130, 121.524834, 2.0}, //學校後門
            {25.041844, 121.525663, 3.0}, //學校前門
            {24.9946021, 121.5831828, 4.0}, //白手長臂猿
            {24.992278, 121.5892527, 5.0} //小爪水獺
    };

    //地理圍欄 測試
    private static boolean hideAnimal=false;

    public void displayHideAnimal(boolean isDisplay){
        if (isDisplay==true) hideAnimal=true;
        else if(isDisplay==false) hideAnimal=false;
    }

    //選擇是否注音
    public static int chooseFont=1;
    public void setFont(int a){
        chooseFont=a;
    }
    public int getFont(){return chooseFont;}

    //學習單資料
    private String WorkSheetList[][]={{"24.9928761","121.5910631","黑腳企鵝"},{"24.9946021","121.5831828","白手長臂猿"},
            {"24.9985962","121.5805931","臺灣動物區"},{"24.9968265","121.5830956","大貓熊館"}};
    private static boolean isFromWS=false;
    private static String position[][]=new String[1][3];

    public void fromWS(int i){
        position[0][0]=WorkSheetList[i][0];
        position[0][1]=WorkSheetList[i][1];
        position[0][2]=WorkSheetList[i][2];
        setIsFromWS(true);
    }
    public void setIsFromWS(boolean a) {isFromWS=a;}
    public boolean getIsFromWS(){ return isFromWS;}
    public String[][] getPositionFromWS(){return position;}


    public boolean getIsHideAnimal(){
        return hideAnimal;
    }

    public String[][] getWorkSheetMarkers(){
        return workSheetMarkers;
    }

    public String[][] getAreaMarkers(){
        return areaMarkers;
    }

    public Double[][] getGeofenceList(){
        return geofenceList;
    }

}
