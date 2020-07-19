package com.e.whatneedtodo;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

public class Music extends AppCompatActivity {
    private static MediaPlayer mp = null;//Initialize player
    public static int resource = R.raw.song_one; //Song chosen
    public static boolean isPlay =false;
    public static void  play(Context context) {
        stop(context);
        mp = MediaPlayer.create(context, resource);
        mp.setLooping(true);
        mp.start();
        if(isPlay){mp.stop();}
    }

    /** Stop the music */
    public static void stop(Context context) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    //Resume the music
    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this);}

    //Stop the music
    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    //Stop the music
    @Override
    public void finish() {
        Music.stop(this);
        super.finish();
    }
}
