package com.ezgo.index;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.unity3d.player.UnityPlayer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

import static android.content.ContentValues.TAG;


public class ArActivity extends UnityPlayerActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    Context context;
    private Handler handler=new Handler();
    //private GifImageView gifImg;
    //private TextView dialogText;
    private boolean voice = true;
    //MusicActivity music;

    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;   // Google API用戶端物件
    private LocationRequest mLocationRequest;   // Location請求物件

    private final Timer timer = new Timer();
    private TimerTask task;

    private ArrayList<Geofence> mGeofenceList = new ArrayList<Geofence>();
    private PendingIntent mGeofencePendingIntent;
    private MyData myData=new MyData();
    private ImageView imageView;
    boolean isHideAnimal;

    private Button sendBtn;
    private String targetPosition[]=new String[2]; //導航目標的位置

    private AlertQuestion alertQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        context=this;
        imageView =(ImageView) findViewById(R.id.testPic);

        //------------開啟Unity----------------
        LinearLayout u3dLayout = (LinearLayout) findViewById(R.id.u3d_layout);
        u3dLayout.addView(mUnityPlayer);
        mUnityPlayer.requestFocus();

/*
        //----------取得螢幕長寬-------------
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int screenHeight = dm.heightPixels ;

        //-----------延遲幾秒後設定--------------------------
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                //過六秒後要做的事情
                gifImg = (GifImageView) findViewById(R.id.gifImg);
                gifImg.setImageResource(R.drawable.elephant_wave); //設定gif圖來源
                gifImg.setMaxHeight((int)(screenHeight*1.0)); //設定gif高

                dialogText = (TextView) findViewById(R.id.dialogText);
                dialogText.setText("你好~導覽要開始囉~");
                dialogText.setBackgroundResource(R.drawable.dialog);

                music = new MusicActivity(context,gifImg,dialogText);

            }}, 6000);
*/

        //---------------取得目標位置---------------------
        Bundle bundle=getIntent().getExtras();
        targetPosition[0]=bundle.getString("targetLat");
        targetPosition[1]=bundle.getString("targetLng");

        //--------------測試android 呼叫 unity 函式------------------
        sendBtn=(Button) findViewById(R.id.sendToUnity);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnityPlayer.UnitySendMessage("Manager", "ZoomIn", "");
            }
        });

        //----------測試用alert選擇題--------
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                alertQuestion=new AlertQuestion(context);
                alertQuestion.showSingleChoiceButton();
            }}, 8000);

        //檢查是否有開啟GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Toast.makeText(context, "請開啟GPS", Toast.LENGTH_LONG).show();
        }

        //連接GOOGLE API
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setButton(); //設定鈕

        checkHideAnimal();  //隱藏動物
    }

    //------------Unity取得目標位置-------------------
    public String[] getTargetPosition() {
        return targetPosition;
    }


    //------------------------------------------是否進入隱藏動物範圍------------------------------------
    public void checkHideAnimal(){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                isHideAnimal=myData.getIsHideAnimal();
                Log.e(TAG,"隱藏動物1:"+isHideAnimal);

                if(isHideAnimal){
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    imageView.setVisibility(View.INVISIBLE);
                }
                super.handleMessage(msg);
            }
        };

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 2000, 2000);
    }

    //------------------------------------------設定鈕------------------------------------
    public void setButton(){
        final ImageButton settingBtn = (ImageButton) findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            //-----------顯示設定選單popupMenu-------------
            final PopupMenu popupMenu = new PopupMenu(ArActivity.this,settingBtn);
            popupMenu.getMenuInflater().inflate(R.menu.activity_ar_setting, popupMenu.getMenu());

            //-------------設定語音按鈕開關圖示----------------------
            MenuItem mi =popupMenu.getMenu().findItem(R.id.voice);
            if (!voice){
                mi.setTitle("開啟語音");
                mi.setIcon(R.drawable.voice_on);
            }else if(voice){
                mi.setTitle("關閉語音");
                mi.setIcon(R.drawable.voice_off);
            }

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.camera:   //------
                        break;
                    case R.id.voice:    //------語音設定

                        if(voice){  //---若語音已開啟則關閉
                            voice=false;
                            //music.setMusic(false);
                        }
                        else{   //---若語音關閉則開啟
                            voice=true;
                            //music.setMusic(true);
                        }

                        break;
                    case R.id.close:    //------結束導覽
                        mUnityPlayer.quit();

                        /*ArActivity.this.finish();
                        Intent intent=new Intent();
                        intent.setClass(context,MainActivity.class);
                        context.startActivity(intent);
                        */
                        break;
                }
                return true;
                }
            });

            MenuPopupHelper menuHelper = new MenuPopupHelper(ArActivity.this, (MenuBuilder) popupMenu.getMenu(), settingBtn);
            menuHelper.setForceShowIcon(true);
            menuHelper.show();
            }
        });
    }

    //---------------加入Geofence--------------
    private void addGeoFence(){
        Double geofenceList[][]=myData.getGeofenceList();

        for (int i=0; i<geofenceList.length; i++){
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId((geofenceList[i][2]).toString())
                    .setCircularRegion(geofenceList[i][0], geofenceList[i][1],25)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    //--------------建立Geofence----------------
    private void startGeofenceMonitoring(){
        try{
            //加入Geofence
            addGeoFence();

            // 建立Geofence請求物件
            GeofencingRequest geofenceRequest = new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofences(mGeofenceList)
                    .build();

            mGeofencePendingIntent = getGeofencePendingIntent();
            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, geofenceRequest,mGeofencePendingIntent)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if(status.isSuccess()){
                                Log.e(TAG, "Successfully added geofence");
                                //Toast.makeText(ArActivity.this,"Geofence成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Log.e(TAG, "Failed to add geofence"+status.getStatus());
                                //Toast.makeText(ArActivity.this,"Geofence失敗",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }catch (SecurityException e){
            Log.e(TAG, "SecurityException - " + e.getMessage());
        }
    }

    private PendingIntent getGeofencePendingIntent(){
        if(mGeofencePendingIntent != null){
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //--------------停止Geofence----------------
    private void stopGeofenceMonitoring(){
        Log.d(TAG, "stopGeofenceMonitoring");
        ArrayList<String> geofenceIds = new ArrayList<String>();
        Double geofenceList[][]=myData.getGeofenceList();

        for(int i=0; i<geofenceList.length; i++){
            geofenceIds.add((geofenceList[i][2]).toString());
        }

        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient,geofenceIds);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();

        // 移除位置請求服務
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            stopGeofenceMonitoring();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // 移除Google API用戶端連線
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // 連線到Google API用戶端
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // 已經連線到Google Services
        // 啟動位置更新服務
        // 位置資訊更新的時候，應用程式會自動呼叫LocationListener.onLocationChanged

        // 建立Location請求物件
        mLocationRequest = new LocationRequest()
                .setInterval(2000)  // 設定讀取位置資訊的間隔時間為一秒（1000ms）
                .setFastestInterval(2000)   // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);   // 設定優先讀取高精確度的位置資訊（GPS）

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        startGeofenceMonitoring();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google Services連線中斷
        // int參數是連線中斷的代號
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Services連線失敗
        // ConnectionResult參數是連線失敗的資訊
        int errorCode = connectionResult.getErrorCode();

        // 裝置沒有安裝Google Play服務
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(this, "google_play_service_missing", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        // 位置改變
        // Location參數是目前的位置
        TextView textView = (TextView) findViewById(R.id.testTv);
        double myLat=location.getLatitude();
        double myLng=location.getLongitude();
        //textView.setText("緯度:"+myLat+"\n經度:"+myLng);
    }


    //------------------------------------------螢幕方向------------------------------------
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 什麼都不用寫
        }
        else {
            // 什麼都不用寫
        }
    }

}
