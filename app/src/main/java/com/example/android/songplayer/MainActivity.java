package com.example.android.songplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private Button play_pause_btn;
    private Button stop_btn;
    private SeekBar seekBar;
    private TextView time;
    private String now_pos;
    String totalTime;
    private boolean durationSet;
    private Handler myHandle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this, R.raw.song);
        play_pause_btn =(Button) findViewById(R.id.play_pause);
        stop_btn = (Button) findViewById(R.id.stop);
        seekBar = (SeekBar) findViewById(R.id.seek_time);
        time = (TextView) findViewById(R.id.time);
        durationSet = false;
        seekBar.setClickable(false);
        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_pause();
            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        if(!durationSet){
            seekBar.setMax(mp.getDuration());
            durationSet=true;
        }
        totalTime = String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) mp.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds((long) mp.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                mp.getDuration())));
        seekBar.setProgress(mp.getCurrentPosition());
        mp.setLooping(true);
        myHandle.postDelayed(updateSongTime,100);
    }
    public void play_pause(){
        if(!mp.isPlaying()){
            mp.start();
            play_pause_btn.setText("\u23F8");
        }else{
            mp.pause();
            play_pause_btn.setText("\u25B6");
        }
    }
    public void stop(){
        mp.stop();
        mp.prepareAsync();
        play_pause_btn.setText("\u25B6");
    }

    private Runnable updateSongTime = new Runnable(){
        public void run() {
            int now = mp.getCurrentPosition();
            now_pos = String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) now),
                    TimeUnit.MILLISECONDS.toSeconds((long) now) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) now)));
            seekBar.setProgress((int)now);
            time.setText(now_pos + "/" + totalTime);
            myHandle.postDelayed(this, 100);
        }};
}