package com.karyasiber.telolet;

import android.content.ContentValues;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main extends AppCompatActivity {
    ImageView btnTelolet;
    MediaPlayer teloletPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        btnTelolet = (ImageView) findViewById(R.id.btnTelolet);
        btnTelolet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    btnTelolet.setImageResource(R.drawable.ic_button_pressed);
                    teloletPlayer = MediaPlayer.create(Main.this, R.raw.telolet);
                    teloletPlayer.start();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    btnTelolet.setImageResource(R.drawable.ic_button_normal);
                    teloletPlayer.reset();
                    teloletPlayer.stop();
                }
                return true;
            }
        });
    }

    public void setRingtone(View v){
        String path = Environment.getExternalStorageDirectory() + "/media/ringtone/";
        String str_song_name = R.raw.telolet + ".mp3";
        String filepath = path + File.separator + str_song_name;
        try {
            File dir = new File(path);
            if (dir.mkdirs() || dir.isDirectory()) {
                CopyRAWtoSDCard(R.raw.telolet , filepath);
            }
            File k = new File(filepath); // path is a file to /sdcard/media/ringtone
            if (k.isFile()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
                values.put(MediaStore.MediaColumns.TITLE, "Telolet");
                values.put(MediaStore.MediaColumns.SIZE, 215454);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                values.put(MediaStore.Audio.Media.ARTIST, "Klakson BUS");
                values.put(MediaStore.Audio.Media.DURATION, 230);
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                values.put(MediaStore.Audio.Media.IS_ALARM, false);
                values.put(MediaStore.Audio.Media.IS_MUSIC, false);

//Insert it into the database
                Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
                Uri newUri = this.getContentResolver().insert(uri, values);

                RingtoneManager.setActualDefaultRingtoneUri(
                        Main.this,
                        RingtoneManager.TYPE_RINGTONE,
                        newUri
                );
                Toast.makeText(Main.this,"Success",Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(Main.this,"Failed",Toast.LENGTH_LONG).show();
        }

    }

    private void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }


}
