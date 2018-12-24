package com.ming.usesystemutils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import ming.com.usesystemutils_lib.UseCamera;

public class MainActivity extends AppCompatActivity {

    private UseCamera useCamera;
    private ImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleImageView = findViewById(R.id.circlrimageview);
        useCamera = new UseCamera(MainActivity.this, new UseCamera.PhotographCallBack() {
            @Override
            public void NoPermission(int requestCode, String[] permissions) {
                Toast.makeText(MainActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void Failure() {
                Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void Cancel() {
                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void Success(Bitmap bitmap) {
                circleImageView.setImageBitmap(bitmap);
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //useCamera.Photograph(Environment.getExternalStorageDirectory()+"/"+System.currentTimeMillis() + ".jpg");
                useCamera.Photograph();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        useCamera.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        useCamera.setRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
