package com.ezgo.index;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = "GTIntentService";
    Handler handler = new Handler();
    MyData myData=new MyData();

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve the Geofencing intent
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        // Handling errors
        if ( event.hasError() ) {
            Log.e(TAG, "Error");
            return;
        }

        // Get the transition type.
        int transition = event.getGeofenceTransition();
        // Get the geofence that were triggered
        List<Geofence> triggeringGeofences = event.getTriggeringGeofences();
        Geofence geofence = triggeringGeofences.get(0);
        final String requestId = geofence.getRequestId();

        // Check if the transition type
        if ( transition == Geofence.GEOFENCE_TRANSITION_ENTER ) {
            Log.e(TAG,"Entering geofence -"+requestId);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), "進入範圍 -"+requestId, Toast.LENGTH_SHORT).show();
                    myData.displayHideAnimal(true);
                }
            });
/*
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "進入範圍 -"+requestId, Toast.LENGTH_SHORT).show();
                }
            });


            new Thread(new Runnable(){
                @Override
                public void run(){
                    Message msg = new Message();
                    msg.what =1;
                    handler.sendMessage(msg);
                    Log.e(TAG,"測試1");
                }
            }).start();
            */


/*
            Intent intent1 = new Intent(this, ArActivity.class);
            intent1.putExtra("isEnter", true);
            startActivity(intent1);
*/
        }else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT ){
            Log.e(TAG,"Exiting geofence -"+requestId);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "離開範圍 -"+requestId, Toast.LENGTH_SHORT).show();
                    myData.displayHideAnimal(false);
                }
            });

        }else{
            Log.e(TAG, getString(transition));
        }


    }
}
