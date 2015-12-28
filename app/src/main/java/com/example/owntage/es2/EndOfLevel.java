package com.example.owntage.es2;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

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

public class EndOfLevel extends Activity {
    private int col;
    private int level;
    private int min;
    private static final String KEY_TOKEN = "vk_token";
    private String id;
    private boolean flage_finish = true, flage_called_finish=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_end_of_level);

        col = getIntent().getIntExtra("result", 0);
        level = getIntent().getIntExtra("level", 0);
        min = getIntent().getIntExtra("minimum", 0);
        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        Button vk = (Button) findViewById(R.id.vk), next = (Button) findViewById(R.id.next);
        TextView txt=(TextView)findViewById(R.id.result);
        if (min > col) {
            vk.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        } else {
            vk.setVisibility(View.VISIBLE);
            if (level < MenuActivity.N)
                next.setVisibility(View.VISIBLE);
            else
                next.setVisibility(View.GONE);
        }
        txt.setText(getResources().getText(R.string.score)+" "+col);
        if(level==MenuActivity.Open && min<=col)
        {
            MenuActivity.Open++;
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent=new Intent(EndOfLevel.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickNext(View view) {
        if(flage_finish)
            finish();

        Intent intent = new Intent(EndOfLevel.this, MainActivity.class);
        intent.putExtra("level_number", level + 1);
        startActivity(intent);
    }

    public void onClickRepeate(View view) {
        if(flage_finish)
            finish();
        Intent intent = new Intent(EndOfLevel.this, MainActivity.class);
        intent.putExtra("level_number", level);
        startActivity(intent);
    }

    public void onClickVK(View view) {
        Button vk = (Button) findViewById(R.id.vk);
        vk.setVisibility(View.GONE);
        flage_finish =false;
        VKAccessToken token = VKAccessToken.tokenFromSharedPreferences(this, KEY_TOKEN);
        if (token != null) {
            onLoginIn(token);
        } else {
            VKSdk.login(this, VKScopes.WALL);
        }
    }

    public void onClickMenu(View view) {
        if(flage_finish)
            finish();
        Intent intent=new Intent(EndOfLevel.this,MenuActivity.class);
        startActivity(intent);
    }

    protected void onLoginIn(VKAccessToken token) {
        Log.i(TAG, "onLoginIn: " + token);
        id = token.userId;
        startCurrentUserRequest();
    }

    protected void onLoginFailed(VKError error) {
        Log.w(TAG, "onLoggedFailed: " + error);
        flage_finish=true;
        Button vk = (Button) findViewById(R.id.vk);
        vk.setVisibility(View.VISIBLE);
        if(flage_called_finish)
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        VKRequest request = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, id, VKApiConst.MESSAGE, "пройден уровень " + level + " в игре PlatformerDefence!"));

        request.attempts = 10;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.i(TAG, "onComplete request");
                flage_finish=true;
                if(flage_called_finish)
                    finish();
            }

            @Override
            public void onError(VKError error) {
                onLoginFailed(error);
            }
        });
    }

    private static final String TAG = "EndOfLevel";
}
