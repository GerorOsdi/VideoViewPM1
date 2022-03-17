package com.app.videoviewpm1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public final int REQUEST_VIDEO_CAPTURE=101;
    VideoView video;
    FloatingActionButton btnCapturar, btnGuardar, btnSaveSQL;
    Uri videoUri;
    Bitmap videoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        video = (VideoView) findViewById(R.id.viewVideo);
        btnCapturar = (FloatingActionButton) findViewById(R.id.btnGrabarVideo);
        //btnGuardar = (FloatingActionButton) findViewById(R.id.btnGuardarVideo);
        btnSaveSQL = (FloatingActionButton) findViewById(R.id.btnSaveSQL);

        btnCapturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });
    }

    private void permisos() {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (videoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(videoIntent,REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            videoUri = data.getData();
            //videoMap = MediaStore.Video.Media.getContentUri();

            if (videoUri != null){
                video.setVideoURI(videoUri);
                video.start();

                try {
                    AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                    FileInputStream inp = videoAsset.createInputStream();
                    FileOutputStream datFile = openFileOutput(nombreArchivo(), Context.MODE_PRIVATE);

                    byte[] buffer = new byte[1024];
                    int leng;

                    while ((leng = inp.read(buffer)) > 0){
                        datFile.write(buffer,0,leng);
                    }
                }catch (IOException ex){
                    Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String nombreArchivo(){
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nameArchivo = fecha + ".mp4";

        return nameArchivo;
    }
}