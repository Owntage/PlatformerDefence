package com.example.owntage.es2;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKScopes;

import com.example.owntage.es2.common.User;
import com.vk.sdk.util.VKUtil;

public class EndOfLevel extends Activity {
    AlertDialog.Builder ad;
    Context context;
    private int col;
    private static final String KEY_TOKEN = "vk_token";
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_end_of_level);



        col=getIntent().getIntExtra("result",0);

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        context=EndOfLevel.this;

        String title="Публикация результатов";
        String message="Хотите опубликовать ВКонтакте результат в "+ Integer.toString(col) +  "?";
        String button1String="Да";
        String buttron2String="Нет";

        ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(button1String, new PositiveOnclickListener());
        ad.setNegativeButton(buttron2String, new NegativeOnClickListener());
        ad.setCancelable(false);

        ad.show();
    }
    private class PositiveOnclickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //вход в вк и публикация
            WriteNode();
            //finish();
        }
    }

    private class NegativeOnClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    private void WriteNode()
    {
        VKAccessToken token=VKAccessToken.tokenFromSharedPreferences(this,KEY_TOKEN);
        if(token!=null)
        {
            onLoginIn(token);
        }
        else
        {
            VKSdk.login(this,VKScopes.WALL);
        }
    }

    protected void onLoginIn(VKAccessToken token)
    {
        Log.i(TAG,"onLoginIn: "+token);
        id=token.userId;
        startCurrentUserRequest();
    }

    protected void onLoginFailed(VKError error) {
        Log.w(TAG, "onLoggedFailed: " + error);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                res.saveTokenToSharedPreferences(EndOfLevel.this, KEY_TOKEN);
                onLoginIn(res);
            }

            @Override
            public void onError(VKError error) {
                onLoginFailed(error);
            }
        });
    }

    @SuppressWarnings("unchecked,deprecation")
    void startCurrentUserRequest() {
        VKRequest request = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, id, VKApiConst.MESSAGE, getResources().getString(R.string.for_wall_before) + " " + col + " " + getResources().getString(R.string.for_wall_after)));
        request.attempts = 10;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.i(TAG, "onComplete request");
                finish();
            }

            @Override
            public void onError(VKError error) {
                onLoginFailed(error);
            }
        });
    }

    private static final String TAG="EndOfLevel";
}
