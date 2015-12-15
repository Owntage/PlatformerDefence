package com.example.owntage.es2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * Created by Owntage on 10/6/2015.
 */
public class MainActivity extends Activity {

    public static String PACKAGE_NAME;
    public static Context CONTEXT;
    MainSurfaceView mainSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("activity", "onCreate");
        PACKAGE_NAME = getPackageName();
        CONTEXT = this;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info =    am.getDeviceConfigurationInfo();
        boolean supportES2 = (info.reqGlEsVersion >= 0x20000);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(supportES2) {
            int levelNumber = getIntent().getIntExtra("level_number",0);
            MainRenderer mainRenderer = new MainRenderer(getString(R.string.vertex_shader), getString(R.string.fragment_shader), this, levelNumber);
            mainSurfaceView = new MainSurfaceView(this, mainRenderer);
            mainSurfaceView.setEGLContextClientVersion(2);
            mainSurfaceView.setRenderer(mainRenderer);
            this.setContentView(mainSurfaceView);
        } else {
            Log.e("OpenGLES 2", "Your device is not supporting gl es 2");
        }
    }

    public void onStart() {
        super.onStart();
        Log.e("activity", "onStart");
    }

    public void onResume() {
        super.onResume();
        mainSurfaceView.onResume();
        Log.e("activity", "onResume");
    }

    public void onPause() {
        super.onPause();
        mainSurfaceView.onPause();
        finish();
        Log.e("activity", "onPause");
    }

    public void onStop() {
        super.onStop();
        Log.e("activity", "onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("activity", "onDestroy");
    }

    public void onGameFinished(int score) {
        Log.e("game is finished", "score: " + score);
        finish();
        Intent intent=new Intent(MainActivity.this,EndOfLevel.class);
        intent.putExtra("result",score);
        startActivity(intent);
    }
}
