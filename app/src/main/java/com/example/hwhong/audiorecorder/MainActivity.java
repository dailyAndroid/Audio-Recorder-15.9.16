package com.example.hwhong.audiorecorder;

import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button play, delete;
    private ImageView spotify;
    private File file;
    private MediaRecorder recorder;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.play);
        delete = (Button) findViewById(R.id.destroy);
        play.setOnClickListener(this);
        delete.setOnClickListener(this);
        play.setEnabled(false);
        delete.setEnabled(false);

        spotify = (ImageView) findViewById(R.id.imageView);
        spotify.setOnTouchListener(new TouchListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.play:
                playFile();
                break;
            case R.id.destroy:
                deleteFile();
                break;
        }
    }

    public class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    try {
                        file = File.createTempFile("raw", ".amr", Environment.getExternalStorageDirectory());
                        recorder = new MediaRecorder();

                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        recorder.setOutputFile(file.getAbsolutePath());
                        recorder.prepare();
                        recorder.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "Recording Starting", Toast.LENGTH_SHORT).show();

                    break;

                case MotionEvent.ACTION_UP:

                    if(recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;

                        play.setEnabled(true);
                        delete.setEnabled(true);

                        playFile();
                        Toast.makeText(getApplicationContext(), "Playing Sound file", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }

            return true;
        }
    }

    private void playFile() {

        File aud_File = new File(file.getAbsolutePath());
        Uri uri = Uri.fromFile(aud_File);
        player = MediaPlayer.create(getApplicationContext(), uri);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                player.start();
            }
        });
    }

    private void deleteFile() {

        file.delete();
        play.setEnabled(false);
        delete.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if(player != null) {
            player.release();
        }
        super.onDestroy();
    }
}
