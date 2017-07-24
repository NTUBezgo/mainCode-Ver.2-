package com.ezgo.index;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private View rootView;
    GoogleMap mMap;

    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;   // Google API用戶端物件
    private LocationRequest mLocationRequest;   // Location請求物件

    private ArrayList<Geofence> mGeofenceList = new ArrayList<Geofence>();
    private PendingIntent mGeofencePendingIntent;
    private MyData myData=new MyData();

    private String mMarkers[][]; //存放座標
    private List<Marker> markerList = new ArrayList<Marker>(); //存放Marker
    private String targetPosition[]=new String[2]; //導航目標的位置
    private LinearLayout arLinearLayout ; //開始導航按鈕

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        try{
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mainMap);
            mapFragment.getMapAsync(MainFragment.this);
        }catch (Exception e){
            e.printStackTrace();
        }

        //檢查是否有開啟GPS
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(getActivity(), "請開啟GPS", Toast.LENGTH_LONG).show();
        }

        //連接GOOGLE API
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        arLinearLayout = (LinearLayout) rootView.findViewById(R.id.btn_ar); //開始導航按鈕
        arLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAr(targetPosition);
            }
        });

        return rootView;
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

            addCircle(geofenceList[i][0], geofenceList[i][1]);//測試用circle
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
                                //Toast.makeText(getActivity(),"Geofence成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Log.e(TAG, "Failed to add geofence"+status.getStatus());
                                //Toast.makeText(getActivity(),"Geofence失敗",Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);

        return PendingIntent.getService(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
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
        Log.d(TAG, "onStart");
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
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();

        // 移除Google API用戶端連線
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        Log.d(TAG, "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();

        // 連線到Google API用戶端
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        Log.d(TAG, "onResume");
        getActivity().setTitle(R.string.app_name); //將標題設為EZ Go
    }

    @Override
    public void onConnected(Bundle bundle) {
        // 已經連線到Google Services啟動位置更新服務，位置資訊更新的時候，應用程式會自動呼叫LocationListener.onLocationChanged

        // 建立Location請求物件
        mLocationRequest = new LocationRequest()
                .setInterval(2000)  // 設定讀取位置資訊的間隔時間為一秒（1000ms）
                .setFastestInterval(5000)   // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);   // 設定優先讀取高精確度的位置資訊（GPS）

        if (ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true); //開啟我的位置圖層
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        Log.d(TAG, "onConnected");

        startGeofenceMonitoring();  //建立Geofence
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google Services連線中斷
        // int參數是連線中斷的代號
        Log.d(TAG, "Google Services連線中斷");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Services連線失敗
        // ConnectionResult參數是連線失敗的資訊
        int errorCode = connectionResult.getErrorCode();

        // 裝置沒有安裝Google Play服務
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(getActivity(), "google_play_service_missing", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        // 位置改變
        // Location參數是目前的位置
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            LatLng taiZoo = new LatLng(24.996241, 121.586054);

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); /*地圖種類*/
            mMap.setMinZoomPreference(12.0f);    //設定偏好的最小縮放層級
            mMap.setMaxZoomPreference(17.0f);   //設定偏好的最大縮放層級

            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setCompassEnabled(true); /*顯示指北針*/
            uiSettings.setMyLocationButtonEnabled(true); /*顯示自己位置按鈕*/

            moveMap(taiZoo);//移動到動物園位置
            addTileOverlay();//新增動物園地圖圖層

            //判斷要顯示的marker
            if(myData.getIsFromWS()){
                jumpWorksheetMarker();  //學習單跳至地圖
            }else{
                chooseAreaMarkers();  //建立各園區marker
            }


            //各園區marker點擊事件
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    arLinearLayout.setVisibility(View.VISIBLE);

                    targetPosition[0]=String.valueOf(marker.getPosition().latitude);
                    targetPosition[1]=String.valueOf(marker.getPosition().longitude);

                    return false;
                }
            });

            //marker資訊視窗點擊事件
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                }
            });

            mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
                @Override
                public void onInfoWindowClose(Marker marker) {
                    arLinearLayout.setVisibility(View.INVISIBLE);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //--------------------------------------------------開始導航----------------------------------------
    private void startAr(String targetPosition[]){
        Intent intent=new Intent();
        Bundle bundle = new Bundle();

        bundle.putString("targetLat",targetPosition[0]);
        bundle.putString("targetLng",targetPosition[1]);

        intent.putExtras(bundle);
        intent.setClass(getActivity(), ArActivity.class);
        startActivity(intent);
    }

    //---------------取得學習單座標---------------
    public void chooseWorkSheetMarkers(){
        removeMarkers();
        mMarkers=myData.getWorkSheetMarkers();
        setMarkers(1);
    }

    //---------------取得所有館區座標---------------
    public void chooseAreaMarkers(){
        removeMarkers();
        mMarkers=myData.getAreaMarkers();
        setMarkers(2);
    }

    //-----------------新增Markers-----------------
    private void setMarkers(int type){
        for(int i=0; i<mMarkers.length; i++){
            LatLng position = new LatLng(Double.parseDouble(mMarkers[i][0]),Double.parseDouble(mMarkers[i][1]));

            MarkerOptions markerOptions = new MarkerOptions();
            if(type==1) markerOptions.position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)); //學習單為旗子Marker
            else if(type==2) markerOptions.position(position); //館區為普通Marker

            Marker marker = mMap.addMarker(markerOptions);
            marker.setTitle(mMarkers[i][2]);

            markerList.add(marker);
        }
    }

    //-----------------移除所有Markers-----------------
    private void removeMarkers(){
        for (Marker marker: markerList) {
            marker.remove();
        }
        markerList.clear();
    }

    //-----------------選取學習單題目跳至其座標-----------------
    public void jumpWorksheetMarker(){
        removeMarkers();
        mMarkers=myData.getPositionFromWS();
        setMarkers(1);
        myData.setIsFromWS(false);
        LatLng position = new LatLng(Double.parseDouble(mMarkers[0][0]),Double.parseDouble(mMarkers[0][1]));
        moveMap(position);
    }

    //-------------------地理圍欄 測試用-----------------------
    private void addCircle(Double lat,Double lng){
        LatLng latLng = new LatLng(lat,lng);

        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(20)
                .strokeWidth(0)
                .strokeColor(Color.argb(200, 255,0,0))
                .fillColor( Color.argb(50, 255,0,0) );
        mMap.addCircle( circleOptions );
    }

    //---------------------------------移動地圖到參數指定的位置-------------------------
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .bearing(147)
                        .zoom(16)
                        .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //----------------------------------新增動物園地圖圖層------------------------------
    private void addTileOverlay(){
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                String s = String.format("http://ezgo.twjoin.com/img/map/%d/%d/%d.png",
                        zoom, x, y);

                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }

                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }

            private boolean checkTileExists(int x, int y, int zoom) {
                int minZoom = 12;
                int maxZoom = 17;

                if ((zoom < minZoom || zoom > maxZoom)) {
                    return false;
                }
                return true;
            }
        };

        TileOverlay tileOverlay = mMap.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider));
    }


    public void onDestroyView()
    {
        try {
            Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.mainMap));
            if (fragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroyView();
    }

}
