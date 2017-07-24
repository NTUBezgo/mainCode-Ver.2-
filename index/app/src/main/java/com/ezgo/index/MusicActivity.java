package com.ezgo.index;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.TextView;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by 8320E on 2017/3/18.
 */

public class MusicActivity{

    private static MediaPlayer mediaPlayer;
    private static Context context;
    private GifImageView gifImg;
    private TextView dialogText;


    public MusicActivity(Context c, GifImageView gim, TextView dt){
        context=c;

        gifImg=gim;
        dialogText=dt;

        initMusic();
    }

    public void initMusic(){
        mediaPlayer = MediaPlayer.create(context, R.raw.hello);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp){
                gifImg.setImageResource(R.drawable.elephant_walkback); //設定gif圖來源
                dialogText.setText("");
                dialogText.setBackgroundResource(android.R.color.transparent);
            }
        });

    }

    public void setMusic(boolean musicSt){
        if(musicSt) {
            mediaPlayer.setVolume(1.0f,1.0f);
        }else{
            mediaPlayer.setVolume(0.0f,0.0f);
        }
    }

}
